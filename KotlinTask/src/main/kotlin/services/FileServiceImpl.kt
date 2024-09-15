package com.akkarimzai.services

import com.akkarimzai.exceptions.ServiceUnavailableException
import com.akkarimzai.models.NewsDto
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import mu.KotlinLogging
import java.io.File
import java.io.FileWriter
import java.io.IOException

class FileServiceImpl(private val csvMapper: CsvMapper) : FileService {
    private val logger = KotlinLogging.logger {}

    override fun exists(path: String): Boolean {
        logger.info("Checking if file exists at path: $path")
        val fileExists = File(path).exists()
        logger.info("File exists: $fileExists")
        return fileExists
    }

    override fun write(path: String, contents: Collection<NewsDto>) {
        logger.info("Writing to file at path: $path with ${contents.size} items.")
        try {
            writeContents(path, contents)
            logger.info("Successfully wrote contents to $path.")
        } catch (e: IOException) {
            logger.error("Error writing to file at $path", e)
            throw ServiceUnavailableException("Error writing to file at $path: ${e.message}")
        }
    }

    private fun writeContents(path: String, contents: Collection<NewsDto>) {
        logger.debug("Preparing to write CSV contents to $path.")
        FileWriter(path).use { writer ->
            csvMapper.writer(csvMapper.schemaFor(NewsDto::class.java).withHeader())
                .writeValues(writer)
                .writeAll(contents)
                .close()
            logger.debug("CSV contents successfully written to $path.")
        };
    }
}