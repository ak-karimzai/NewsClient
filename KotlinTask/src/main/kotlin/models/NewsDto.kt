package com.akkarimzai.models

data class NewsDto(
    val id: Int,
    val title: String,
    var place: String,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int
)