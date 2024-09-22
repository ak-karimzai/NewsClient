package services

import com.akkarimzai.exceptions.ValidationException
import com.akkarimzai.repositories.NewsRepository
import com.akkarimzai.services.FileService
import com.akkarimzai.services.NewsService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import mockedRepositories.MockNewsRepository

class NewsServiceListTest : FunSpec() {
    init {
        context("News service list method tests") {
            test("count less than or equal to zero throw validation exception") {
                // Arrange
                val count = -1

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.list(count)
                }
            }

            test("count greater than 100 throw validation exception") {
                // Arrange
                val count = -1

                // Act && Assert
                shouldThrow<ValidationException> {
                    newsService.list(count)
                }
            }

            test("valid page size normally return a list of newlines") {
                // Arrange
                val count = 10

                // Act
                val list = newsService.list(count)

                // Assert
                list.size shouldBe 10
            }
        }
    }
    private var mockedRepository: NewsRepository = MockNewsRepository();
    private var mockedService: FileService = mockk();
    private val newsService: NewsService = NewsService(mockedRepository, mockedService)
}