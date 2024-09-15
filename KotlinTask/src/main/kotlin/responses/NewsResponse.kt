package com.akkarimzai.responses

import com.akkarimzai.entities.News
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val results: List<News>
)