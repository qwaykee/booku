package com.qwaykee.booku.data.repository

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.qwaykee.booku.data.models.Author
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.models.OnlineLibrary
import com.qwaykee.booku.data.network.NetworkHelper
import io.realm.kotlin.ext.realmListOf

@Suppress("RemoveExplicitTypeArguments", "unused")
class OnlineLibraryRepository(
    private val networkHelper: NetworkHelper,
    private val config: OnlineLibrary
) {
    fun getBookFromISBN(isbn: String): Book? {
        val url = (config.urls?.baseUrl ?: "") + config.urls?.isbnQuery?.let { String.format(it, isbn) }
        return getBookFromURL(url)
    }

    fun getBookFromURL(url: String): Book? {
        val response = networkHelper.fetchDataFromUrl(url)

        return response?.let {
            when (config.selectors!!.responseType) {
                "JSON" -> jsonToBook(JsonPath.parse(it.toString()))
                "HTML" -> htmlToBook(Ksoup.parse(it.toString()))
                else -> throw Exception("Unsupported response type")
            }
        }
    }

    fun getSearchResults(query: String): List<Book> {
        val url = (config.urls?.baseUrl ?: "") + config.urls?.let { String.format(it.searchQuery, query) }
        val response = networkHelper.fetchDataFromUrl(url)

        return when (config.selectors!!.responseType) {
            "JSON" -> parseJsonResponse(response.toString())
            "HTML" -> parseHtmlResponse(response.toString())
            else -> throw Exception("Unsupported response type")
        }
    }

    private fun jsonToBook(document: DocumentContext): Book {
        val selectors = config.selectors!!.bookSelectors!!

        return Book().apply {
            title = document.read<String>(selectors.title)
            selectors.author?.let {
                author = Author().apply {
                    name = document.read<String>(it.name)
                    description = document.read<String>(it.description)
                    language = document.read<String>(it.language)
                    imageURL = document.read<String>(it.imageURL)
                    wikipediaURL = document.read<String>(it.wikipediaURL)
                }
            }
            description = document.read<String>(selectors.description)
            year = document.read<Int>(selectors.year)
            language = document.read<String>(selectors.language)
            pages = document.read<Int>(selectors.pages)
            publisher = document.read<String>(selectors.publisher)
            edition = document.read<String>(selectors.edition)
            isbn = document.read<String>(selectors.isbn)
            imagePath = document.read<String>(selectors.imageURL)
            // TODO: Fix
            downloadMirrors = realmListOf(document.read<String>(selectors.downloadMirrors.first()))
            fileSize = document.read<Int>(selectors.fileSize)
            fileExtension = document.read<String>(selectors.fileExtension)
        }
    }

    private fun htmlToBook(document: Document): Book {
        val selectors = config.selectors!!.bookSelectors!!

        return Book().apply {
            title = document.selectFirst(selectors.title)?.text() ?: ""
            selectors.author?.let {
                author = Author().apply {
                    name = document.selectFirst(it.name)?.text() ?: ""
                    description = document.selectFirst(it.description ?: "")?.text() ?: ""
                    language = document.selectFirst(it.language ?: "")?.text() ?: ""
                    imageURL = document.selectFirst(it.imageURL ?: "")?.text() ?: ""
                    wikipediaURL = document.selectFirst(it.wikipediaURL ?: "")?.text() ?: ""
                }
            }
            description = document.selectFirst(selectors.description ?: "")?.text() ?: ""
            year = document.selectFirst(selectors.year ?: "")?.text()?.toInt() ?: 0
            language = document.selectFirst(selectors.language ?: "")?.text() ?: ""
            pages = document.selectFirst(selectors.pages ?: "")?.text()?.toInt() ?: 0
            publisher = document.selectFirst(selectors.publisher ?: "")?.text() ?: ""
            edition = document.selectFirst(selectors.edition ?: "")?.text() ?: ""
            isbn = document.selectFirst(selectors.isbn ?: "")?.text() ?: ""
            imagePath = document.selectFirst(selectors.imageURL ?: "")?.text() ?: ""
            // TODO: Fix
            downloadMirrors = realmListOf(
                    document.selectFirst(selectors.downloadMirrors.firstOrNull() ?: "")?.text() ?: ""
                    )
            fileSize = document.selectFirst(selectors.fileSize ?: "")?.text()?.toInt() ?: 0
            fileExtension = document.selectFirst(selectors.fileExtension ?: "")?.text() ?: ""
        }
    }

    private fun parseJsonResponse(json: String): List<Book> {
        return JsonPath
            .parse(json)
            .read<List<Map<String, Any>>>(config.selectors?.searchSelectors?.eachBookSelector ?: "")
            .map { jsonToBook(it as DocumentContext) }
    }

    private fun parseHtmlResponse(html: String): List<Book> {
        return Ksoup
            .parse(html)
            .select(config.selectors?.searchSelectors?.eachBookSelector ?: "")
            .map { htmlToBook(it as Document) }
    }
}