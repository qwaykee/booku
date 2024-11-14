package com.qwaykee.booku.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkHelper {
    // TODO: Proxy support
    private val client = OkHttpClient() // OkHttpClient.Builder().proxy().build()

    // TODO: bufferSizeLimit support
    suspend fun fetchDataFromUrl(url: String): ByteArray? {
        return when (URLHelper().parseUrl(url)) {
            URLHelper.URLType.HTTP -> fetchDataFromHttp(url)
            URLHelper.URLType.IPFS -> fetchDataFromIpfs(url)
            URLHelper.URLType.MAGNET -> fetchDataFromMagnet(url)
            URLHelper.URLType.UNKNOWN -> null
        }
    }

    private suspend fun fetchDataFromHttp(url: String): ByteArray? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                response.body?.bytes()
            } else {
                null
            }
        }
    }

    // TODO: Finish
    private suspend fun fetchDataFromIpfs(url: String): ByteArray? = withContext(Dispatchers.IO) {
        null
    }

    // TODO: Finish
    private suspend fun fetchDataFromMagnet(url: String): ByteArray? = withContext(Dispatchers.IO) {
        null
    }
}