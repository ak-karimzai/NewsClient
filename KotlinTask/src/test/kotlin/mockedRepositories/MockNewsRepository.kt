package mockedRepositories

import com.akkarimzai.entities.News
import com.akkarimzai.repositories.NewsRepository

class MockNewsRepository : NewsRepository {
    override suspend fun list(size: Int, page: Int): List<News> {
        return getMockedNews();
    }

    companion object {
        fun getMockedNews(): List<News> = listOf(
            News(
                id = 1,
                publicationDate = 1633036800,
                title = "Art Exhibition",
                place = News.Place("Paris"),
                description = "A modern art exhibition in the heart of Paris.",
                siteUrl = "https://example.com/art-exhibition",
                favoritesCount = 250,
                commentsCount = 45
            ),
            News(
                id = 2,
                publicationDate = 1635705600,
                title = "Music Festival",
                place = News.Place("New York"),
                description = "A weekend-long music festival with top artists.",
                siteUrl = "https://example.com/music-festival",
                favoritesCount = 1500,
                commentsCount = 350
            ),
            News(
                id = 3,
                publicationDate = 1726951352,
                title = "Tech Conference",
                place = News.Place("Berlin"),
                description = "A gathering of the brightest minds in tech.",
                siteUrl = "https://example.com/tech-conference",
                favoritesCount = 600,
                commentsCount = 120
            ),
            News(
                id = 4,
                publicationDate = 1640995200,
                title = "Food Fair",
                place = News.Place("Tokyo"),
                description = "A celebration of street food from around the world.",
                siteUrl = "https://example.com/food-fair",
                favoritesCount = 800,
                commentsCount = 220
            ),
            News(
                id = 5,
                publicationDate = 1643673600,
                title = "Film Screening",
                place = News.Place("London"),
                description = "Exclusive screening of an award-winning film.",
                siteUrl = "https://example.com/film-screening",
                favoritesCount = 300,
                commentsCount = 80
            ),
            News(
                id = 6,
                publicationDate = 1646092800,
                title = "Book Launch",
                place = News.Place("San Francisco"),
                description = "Launch event for a bestselling author's new book.",
                siteUrl = "https://example.com/book-launch",
                favoritesCount = 450,
                commentsCount = 150
            ),
            News(
                id = 7,
                publicationDate = 1648771200,
                title = "Photography Workshop",
                place = News.Place("Sydney"),
                description = "A hands-on workshop for budding photographers.",
                siteUrl = "https://example.com/photo-workshop",
                favoritesCount = 275,
                commentsCount = 60
            ),
            News(
                id = 8,
                publicationDate = 1651363200,
                title = "Fashion Show",
                place = News.Place("Milan"),
                description = "An exclusive look at the latest fashion trends.",
                siteUrl = "https://example.com/fashion-show",
                favoritesCount = 1000,
                commentsCount = 300
            ),
            News(
                id = 9,
                publicationDate = 1654041600,
                title = "Gaming Convention",
                place = News.Place("Los Angeles"),
                description = "A massive convention for gaming enthusiasts.",
                siteUrl = "https://example.com/gaming-convention",
                favoritesCount = 2000,
                commentsCount = 500
            ),
            News(
                id = 10,
                publicationDate = 1656633600,
                title = "Charity Run",
                place = null,
                description = "A charity run to support local causes.",
                siteUrl = "https://example.com/charity-run",
                favoritesCount = 650,
                commentsCount = 90
            )
        )
    }
}