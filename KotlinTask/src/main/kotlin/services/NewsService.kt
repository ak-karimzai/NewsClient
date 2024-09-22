package com.akkarimzai.services

import com.akkarimzai.entities.News
import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.models.NewsDto
import com.akkarimzai.profiles.toDto
import com.akkarimzai.repositories.NewsRepository
import mu.KotlinLogging
import java.time.LocalDate

class NewsService(private val newsRepository: NewsRepository, private val fileService: FileService) {
    private val logger = KotlinLogging.logger {}

    suspend fun list(count: Int): List<NewsDto> {
        if (count <= 0 || count > 100) {
            logger.warn { "Invalid page size requested: $count" }
            throw ValidationException("invalid page size: $count")
        }

        logger.info { "Listing news with count: $count" }

        return newsRepository.list(count, 1)
            .map { it.toDto() }
            .toList().also {
                logger.debug { "Returning ${it.size} news items." }
            }
    }

    suspend fun listMostRated(count: Int, period: ClosedRange<LocalDate>): List<NewsDto> {
        if (count <= 0) {
            logger.warn { "Invalid count requested: $count" }
            throw ValidationException("invalid count: $count")
        }

        if (period.start > period.endInclusive) {
            logger.warn { "Invalid period: start=${period.start}, end=${period.endInclusive}" }
            throw ValidationException("invalid period: start=${period.start} end=${period.endInclusive}")
        }

        logger.info { "Fetching most rated news with count: $count, for period: $period" }

        val result: MutableList<News> = mutableListOf();
        var startPage = periodStartPage(period)

        do {
            logger.debug { "Fetching news from page $startPage" }
            val list: List<News> = newsRepository.list(100, startPage++)
            val filteredNews: List<News> = list.filter { it.getPublicationDate() in period }

            result.addAll(filteredNews)
            logger.debug { "Filtered ${filteredNews.size} news items in period $period" }
        } while (filteredNews.isNotEmpty())

        return result
            .sortedByDescending { it.rating }
            .take(count)
            .map { it.toDto() }
            .toList().also {
                logger.debug { "Returning ${it.size} top-rated news items." }
            }
    }

    private suspend fun periodStartPage(period: ClosedRange<LocalDate>): Int {
        var page: Int = 1
        val size: Int = 100

        logger.info { "Determining period start page for period: $period" }
        do {
            logger.debug { "Checking page $page for news in period $period" }
            val news: List<News> = newsRepository.list(size, page++)
        } while (!news.any{ it.getPublicationDate() in period })

        return if (page > 1) page - 1 else page.also {
            logger.debug { "Found starting page: $it for period: $period" }
        }
    }

    suspend fun save(path: String, news: Collection<NewsDto>) {
        if (path.isEmpty()) {
            logger.warn { "Invalid path provided: path is empty" }
            throw ValidationException("invalid path: $path")
        }

        if (fileService.exists(path)) {
            logger.warn { "File already exists at path: $path" }
            throw ValidationException("File already exists: $path")
        }

        logger.info { "Saving ${news.size} news items to path: $path" }
        fileService.write(path, news)
    }
}