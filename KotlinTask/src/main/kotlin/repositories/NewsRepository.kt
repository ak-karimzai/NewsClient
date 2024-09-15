package com.akkarimzai.repositories

import com.akkarimzai.entities.News

interface NewsRepository {
    suspend fun list(size: Int = 100, page: Int = 1): List<News>
}