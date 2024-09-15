package com.akkarimzai.profiles

import com.akkarimzai.entities.News
import com.akkarimzai.models.NewsDto

fun News.toDto(): NewsDto {
    val place = this.place?.title ?: "Unknown"
    return NewsDto(
        this.id,
        this.title,
        place,
        this.description,
        this.siteUrl,
        this.favoritesCount,
        this.commentsCount
    );
}
