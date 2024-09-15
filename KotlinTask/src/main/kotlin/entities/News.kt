package com.akkarimzai.entities

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.exp

@Serializable
data class News(
    val id: Int,
    val publicationDate: Long,
    val title: String,
    val place: Place?,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int
) {
    @Serializable
    data class Place(val title: String)

    val rating: Double by lazy {
        1.0 / (1.0 + exp((-(favoritesCount / (commentsCount + 1))).toDouble()))
    }

    fun getPublicationDate(): LocalDate {
        val instant = Instant.ofEpochSecond(publicationDate)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())

        return zonedDateTime.toLocalDate()
    }
}