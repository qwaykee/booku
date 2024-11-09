package com.qwaykee.booku.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ln
import kotlin.math.pow

@Suppress("unused")
class Book : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    // book information
    var title: String = ""
    var author: Author? = null
    var description: String = ""
    var year: Int = 0
    var language: String = ""
    var pages: Int = 0
    var publisher: String = ""
    var edition: String = ""
    var isbn: String = "" // will be converted to isbn 13 if is isbn 10

    // audiobook information
    var isAudio: Boolean = false
    var length: Long = 0 // in seconds
    var quality: Int = 0 // in kbps
    var readBy: String = ""

    // user information
    var readingProgression: Int = 0 // pages count
    var isFavorite: Boolean = false
    var isCompressed: Boolean = false
    var lastReadDate: Long = 0 // timestamp in milliseconds
    var collection: Collection? = null

    // source information
    // if all are empty, file is local
    var downloadURLFromIPFS: String? = null
    var downloadURLFromTorrent: String? = null
    var downloadURLFromHTTP: String? = null
    var originalURL: String = "" // link to page where the book was found

    // file on disk information
    var filePath: String = ""
    var fileSize: Int = 0 // in bytes
    var fileExtension: String = ""
    var imagePath: String = ""

    fun getReadingProgressionPercentage(): Int {
        return if (pages > 0) {
            (readingProgression * 100) / pages
        } else {
            0 // Return 0 if the total pages are zero to avoid division by zero
        }
    }

    fun lastReadDateText(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(lastReadDate))
    }

    fun formattedFileSize(): String {
        if (fileSize < 1024) return "$fileSize B"

        val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
        val exp = (ln(fileSize.toDouble()) / ln(1024.0)).toInt()
        val size = fileSize / 1024.0.pow(exp.toDouble())

        return String.format("%.2f %s", size, units[exp])
    }
}