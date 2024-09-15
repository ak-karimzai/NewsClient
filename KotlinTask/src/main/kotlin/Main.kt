package com.akkarimzai

import com.akkarimzai.exceptions.NotFoundException
import com.akkarimzai.exceptions.ServiceUnavailableException
import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.repositories.NewsRepository
import com.akkarimzai.repositories.NewsRepositoryImpl
import com.akkarimzai.services.FileService
import com.akkarimzai.services.FileServiceImpl
import com.akkarimzai.services.NewsService
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import mu.KotlinLogging
import java.time.LocalDate
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main(args: Array<String>) {
    if (args.isEmpty() || args.size < 2) {
        println("""
            usage: app.jar [command] [...args]
                1.list: 
                        args:
                            1. [count]
                            2. [dest path] -> default stdout
                2.list-rated: 
                        args:
                            1. [count]
                            2. [start-period] (yyyy-MM-dd)
                            3. [end-period] (yyyy-MM-dd)
                            4. [dest path] -> default stdout
        """.trimIndent())
        exitProcess(-1)
    }
    val logger = KotlinLogging.logger {}
    val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    namingStrategy = JsonNamingStrategy.SnakeCase
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    val csvMapper: CsvMapper = CsvMapper().apply {
        registerKotlinModule()
    }
    val repository: NewsRepository = NewsRepositoryImpl(client)
    val fileService: FileService = FileServiceImpl(csvMapper)
    val newsService: NewsService = NewsService(repository, fileService)

    try {
        runCommand(args, newsService)
    } catch (e: Exception) {
        when (e) {
            is NotFoundException, is ServiceUnavailableException, is ValidationException -> {
                logger.error { "Request processing error: ${e.message}" }
            }
            else -> {
                logger.error { "An error occurred while processing command: ${e.message}" }
            }
        }
    } finally {
        client.close()
    }

}

suspend fun runCommand(args: Array<String>, newsService: NewsService) {
    when (args[0]) {
        "list" -> {
            val count: Int = args[1].toInt()
            val news = newsService.list(count)
            if (args.size == 3) {
                newsService.save(args[2], news)
            } else {
                println(news)
            }
        }
        "list-rated" -> {
            val count: Int = args[1].toInt()
            val startDate: LocalDate = LocalDate.parse(args[2])
            val endDate: LocalDate = LocalDate.parse(args[3])
            val ratedNews = newsService.listMostRated(count, startDate..endDate)
            if (args.size == 5) {
                val dstPath = args[4]
                newsService.save(dstPath, ratedNews)
            } else {
                println(ratedNews)
            }
        }
    }
}