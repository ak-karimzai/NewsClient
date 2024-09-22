package com.akkarimzai.dsl

interface NewsElement {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(val text: String) : NewsElement {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

@DslMarker
annotation class NewsTagMarker

@NewsTagMarker
abstract class NewsTag(val name: String) : NewsElement {
    protected val children = mutableListOf<NewsElement>()

    protected fun <T : NewsElement> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent<$name>\n")
        for (child in children) {
            child.render(builder, "$indent  ")
        }
        builder.append("$indent</$name>\n")
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class NewsTagWithText(name: String) : NewsTag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

class NewsArticle : NewsTagWithText("news") {
    fun id(init: Id.() -> Unit) = initTag(Id(), init)
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
    fun place(init: Place.() -> Unit) = initTag(Place(), init)
    fun description(init: Description.() -> Unit) = initTag(Description(), init)
    fun siteUrl(init: SiteUrl.() -> Unit) = initTag(SiteUrl(), init)
    fun favoritesCount(init: FavoritesCount.() -> Unit) = initTag(FavoritesCount(), init)
    fun commentsCount(init: CommentsCount.() -> Unit) = initTag(CommentsCount(), init)
}

class Id : NewsTagWithText("id")
class Title : NewsTagWithText("title")
class Place : NewsTagWithText("place")
class Description : NewsTagWithText("description")
class SiteUrl : NewsTagWithText("site-url")
class FavoritesCount : NewsTagWithText("favorites-count")
class CommentsCount : NewsTagWithText("comments-count")

fun newsArticle(init: NewsArticle.() -> Unit): NewsArticle {
    val article = NewsArticle()
    article.init()
    return article
}