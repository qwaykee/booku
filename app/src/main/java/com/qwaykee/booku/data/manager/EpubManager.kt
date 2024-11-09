package com.qwaykee.booku.data.manager

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.qwaykee.booku.data.models.Author
import com.qwaykee.booku.data.models.Book
import java.io.File
import java.time.LocalDate
import java.util.zip.ZipFile

class EpubManager : FileManager {
    override fun retrieveMetadata(file: File) : Book {
        val book = Book()

        getOPFDocument(file)?.let {
            book.apply {
                title = it.select("metadata > dc|title").text()
                author = Author().apply {
                    name = it.select("metadata > dc|creator").text()
                }
                description = it.select("metadata > dc|description").text()
                year = LocalDate.parse(
                    it.select("metadata > dc|date").text()
                ).year
                language = it.select("metadata > dc|language").text()
                publisher = it.select("metadata > dc|publisher").text()
                isbn = it.select("metadata > dc|identifier").text()
            }
        }

        return book
    }

    @Composable
    override fun render(context: Context, file: File, parameters: RenderingParameters) {
        AndroidView(factory = {
            WebView(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }, update = { webView ->
            ZipFile(file).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    if (entry.name == retrieveChapters(file)[0]) {
                        val content = zip
                            .getInputStream(entry)
                            .bufferedReader()
                            .use { it.readText() }

                        webView.loadData(content, "application/xhtml+xml", "utf-8")
                    }
                }
            }
        })
    }

    private fun getOPFDocument(file: File): Document? {
        ZipFile(file).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (entry.name.endsWith(".opf")) {
                    val content = zip
                        .getInputStream(entry)
                        .bufferedReader()
                        .use { it.readText() }

                    return Ksoup.parse(content)
                }
            }
        }

        return null
    }

    private fun retrieveChapters(file: File) : MutableList<String> {
        val chapters = mutableListOf<String>()

        getOPFDocument(file)
            ?.select("manifest > item[media-type=\"application/xhtml+xml\"]")
            ?.forEach { element ->
                chapters.add(element.attr("href"))
            }

        return chapters
    }

    override fun compress(file: File) {
        TODO("Not yet implemented")
    }
}