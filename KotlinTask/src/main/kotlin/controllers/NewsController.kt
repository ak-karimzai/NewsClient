package com.akkarimzai.controllers

import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.models.NewsDto
import com.akkarimzai.services.FileService
import com.akkarimzai.services.NewsService
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import mu.KotlinLogging

class NewsController(
    private val fileService: FileService,
    private val newsService: NewsService,
) {
    private val logger = KotlinLogging.logger {}

    private suspend fun worker(id: Int,
                               page: Int,
                               pageSize: Int,
                               channel: Channel<List<NewsDto>>,
                               dispatcher: CoroutineDispatcher
    ) = withContext(dispatcher) {
        try {
            val news = newsService.list(pageSize, page)
            logger.info { "Worker $id fetched ${news.size} items from page $page." }
            channel.send(news)
        } catch (e: Exception) {
            logger.error(e) { "Worker $id failed to fetch news from page $page." }
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun CoroutineScope.processorActor(path: String,
                                              channel: Channel<List<NewsDto>>
    ) = actor<List<NewsDto>> {
        fileService.exists(path).let { exists ->
            if (exists) throw ValidationException("File already exists: $path")
        }

        val allNews = mutableListOf<NewsDto>()

        for (newsList in channel) {
            logger.info { "Processor received ${newsList.size} items to write." }
            allNews.addAll(newsList)
        }

        logger.info { "Writing all collected news to file." }
        fileService.write(path, allNews)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun run(path: String,
                    count: Int,
                    workerCount: Int
    ) = coroutineScope {
        val channel = Channel<List<NewsDto>>()
        val processor = processorActor(path, channel)
        val dispatcher = newFixedThreadPoolContext(workerCount, "workerPool")

        val workers = mutableListOf<Job>()

        var workerNumber = 1
        var pageNumber = 1
        var totalNews = count
        while (totalNews > 0) {
            val pageSize = if (totalNews >= 100) { 100 } else { totalNews }
            workers.add(launch {
                worker(workerNumber++, pageNumber++, pageSize, channel, dispatcher)
            })
            totalNews -= 100
        }

        workers.forEach { it.join() }

        channel.close()

        processor.close()
    }
}