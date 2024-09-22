package services

import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.profiles.toDto
import com.akkarimzai.repositories.NewsRepository
import com.akkarimzai.services.FileService
import com.akkarimzai.services.NewsService
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import mockedRepositories.MockNewsRepository

class NewsServiceSaveTest : FunSpec() {
    init {
        context("News service save method tests") {
            beforeTest() {
                clearMocks(mockedService)
                clearMocks(mockedRepository)
            }

            test("empty path throws validation exception") {
                // Arrange
                val filePath = ""

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.save(filePath, newsList)
                }
            }

            test("when file exists throws validation exception") {
                // Arrange
                val filePath = "./file.csv"
                every { mockedService.exists(filePath) } returns true

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.save(filePath, newsList)
                }
            }

            test("function executes normally when everything is correct") {
                // Arrange
                val filePath = "./file.csv"
                every { mockedService.exists(filePath) } returns false
                every { mockedService.write(filePath, any()) } just Runs

                // Act && Assert
                shouldNotThrowAny {
                    newsService.save(filePath, newsList)
                }
                verify(exactly = 1) { mockedService.write(filePath, any()) }
            }
        }
    }

    private var mockedRepository: NewsRepository = mockk();
    private var mockedService: FileService = mockk();
    private val newsService: NewsService = NewsService(mockedRepository, mockedService)
    private val newsList = MockNewsRepository.getMockedNews()
        .map { it.toDto() }
        .toList()
}