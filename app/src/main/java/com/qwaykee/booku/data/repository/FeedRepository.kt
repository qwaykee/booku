package com.qwaykee.booku.data.repository

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Elements
import com.qwaykee.booku.data.models.Feed
import com.qwaykee.booku.data.models.FeedEntry
import com.qwaykee.booku.data.network.NetworkHelper

class FeedRepository(private val networkHelper: NetworkHelper = NetworkHelper()) {
    fun getFeed(url: String): Feed {
        // TODO: Handle empty or null texts in selectFirst().text()!!
        // should support rss and atom feeds
        val response = networkHelper.fetchDataFromUrl(url)
        val document = Ksoup.parse(response.toString())

        val feed = Feed().apply {
            title = document.selectFirst("channel > title, feed > title")?.text()!!
            description = document.selectFirst("channel > description, feed > subtitle")?.text()!!
            lastClientUpdateDate = System.currentTimeMillis()
            feedUrl = url
            websiteUrl = document.selectFirst("channel > link, feed > link[rel=\"self\"]")?.attr("href")!!
            iconUrl = document.selectFirst("channel > image, feed > icon")?.text()!!
        }

        feed.entries.addAll(parseFeedEntries(document.select("channel > item, feed > entry")))

        return feed
    }

    fun getEntries(feed: Feed): List<FeedEntry> {
        val response = networkHelper.fetchDataFromUrl(feed.feedUrl)
        val document = Ksoup.parse(response.toString())
        return parseFeedEntries(document.select("channel > item, feed > entry"))
    }

    private fun parseFeedEntries(entries: Elements): List<FeedEntry> {
        return entries.map { entry ->
            val url = entry.selectFirst("link")
                ?.run { attr("href").takeIf { it.isNotEmpty() } ?: text() }!!

            FeedEntry().apply {
                title = entry.selectFirst("title")?.text()!!
                articleUrl = url
                description = entry.selectFirst("description, summary")?.text()!!
                publishDate = entry.selectFirst("pubDate, published")?.text()?.toLong()!! // TODO: Parse date properly
                updateDate = entry.selectFirst("updated")?.text()?.toLong()!! // TODO: Parse date properly
                content = entry.selectFirst("content")?.text()
                    ?: networkHelper.fetchDataFromUrl(url).toString() // TODO: Parse html properly
            }
        }
    }
}