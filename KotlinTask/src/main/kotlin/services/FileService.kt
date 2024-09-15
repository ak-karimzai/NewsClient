package com.akkarimzai.services

import com.akkarimzai.models.NewsDto

interface FileService {
    fun exists(path: String): Boolean
    fun write(path: String, contents: Collection<NewsDto>)
}