package com.qwaykee.booku.data.network

class URLHelper {
    enum class URLType { IPFS, MAGNET, HTTP, UNKNOWN }

    fun parseUrl(url: String): URLType {
        return when {
            isIpfsUrl(url) -> URLType.IPFS
            isMagnetUrl(url) -> URLType.MAGNET
            isHttpUrl(url) -> URLType.HTTP
            else -> URLType.UNKNOWN
        }
    }

    private fun isIpfsUrl(url: String): Boolean {
        val ipfsRegex = Regex("^ipfs://[a-zA-Z0-9]+$")
        return ipfsRegex.matches(url)
    }

    private fun isMagnetUrl(url: String): Boolean {
        val magnetRegex = Regex("^magnet:\\?xt=urn:[a-zA-Z0-9]+:[a-zA-Z0-9]{32,40}.*$")
        return magnetRegex.matches(url)
    }

    private fun isHttpUrl(url: String): Boolean {
        val httpRegex = Regex("^(http|https)://[\\w-]+(\\.[\\w-]+)+.*$")
        return httpRegex.matches(url)
    }

}