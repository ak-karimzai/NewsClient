package services

import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.repositories.NewsRepository
import com.akkarimzai.services.FileService
import com.akkarimzai.services.NewsService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import mockedRepositories.MockNewsRepository
import java.time.LocalDate

class NewsServiceListMostRatedTest : FunSpec() {
    init {
        context("News service list most rated method tests") {
            test("count less than zero throws validation exception") {
                // Arrange
                val count = -1
                val before = LocalDate.of(2020, 1, 1)
                val now = LocalDate.now()

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.listMostRated(count, before..now)
                }
            }

            test("invalid period throws validation exception") {
                // Arrange
                val count = 100
                val before = LocalDate.of(2020, 1, 1)
                val now = LocalDate.now()

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.listMostRated(count, now..before)
                }
            }

            test("valid list most didn't works normally when repository gives all news in period") {
                // Arrange
                val count = 10
                coEvery { mockedRepository.list(100, any()) } returns listOf()
                coEvery { mockedRepository.list(100, 1) } returns MockNewsRepository.getMockedNews()
                val before = LocalDate.of(2020, 1, 1)
                val now = LocalDate.now()

                // Act
                val list = newsService.listMostRated(count, before..now)

                // Assert
                list.size shouldNotBe 0
            }
        }
    }
    private var mockedRepository: NewsRepository = mockk();
    private var mockedService: FileService = mockk();
    private val newsService: NewsService = NewsService(mockedRepository, mockedService)
}