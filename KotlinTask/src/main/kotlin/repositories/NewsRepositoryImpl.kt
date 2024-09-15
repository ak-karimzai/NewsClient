package com.akkarimzai.repositories

import com.akkarimzai.entities.News
import com.akkarimzai.exceptions.ServiceUnavailableException
import com.akkarimzai.responses.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.TimeoutCancellationException
import mu.KotlinLogging

class NewsRepositoryImpl(private val client: HttpClient) : NewsRepository {
    private val logger = KotlinLogging.logger {}

    override suspend fun list(size: Int, page: Int): List<News> {
        val response: NewsResponse

        try {
            response = fetchNews(size, page)
        } catch (e: TimeoutCancellationException) {
            logger.debug { "request timeout: $e" }
            throw ServiceUnavailableException("request delay timeout")
        } catch (e: Exception) {
            logger.debug { "something went wrong: $e" }
            throw ServiceUnavailableException("something wrong in request")
        }

        return response.results
    }

    private suspend inline fun fetchNews(size: Int, page:Int): NewsResponse {
        logger.info { "request accepted with size: $size page: $page" }
        val response = client.get {
            url("https://kudago.com/public-api/v1.4/news")
            parameter("page", page)
            parameter("page_size", size)
            parameter("location", "msk")
            parameter("expand", "place")
            parameter("text_format", "plain")
            parameter("fields", "id,publication_date,title,place,description,site_url,favorites_count,comments_count")
        }
        logger.info { "request {${response.request}} response status: ${response.status}" }
        return response.body<NewsResponse>()
    }
}