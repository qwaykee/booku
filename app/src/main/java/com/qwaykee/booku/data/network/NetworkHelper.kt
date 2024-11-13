package com.qwaykee.booku.data.network

import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkHelper {
    // TODO: Proxy support
    private val client = OkHttpClient() // OkHttpClient.Builder().proxy().build()

    fun fetchDataFromUrl(url: String, bufferSizeLimit: Int? = null): ByteArray? {
        // TODO: bufferSizeLimit support
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            response.body?.bytes()
        } else {
            null
        }
    }
}