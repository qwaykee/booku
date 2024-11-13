@file:Suppress("unused")

package com.qwaykee.booku.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

// whole json file should not exceed 32kb
class OnlineLibrary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()

    var name: String = ""
    var parameters: RealmList<Parameters> = realmListOf()
    var bodyQuery: String? = null
    var urls: UrlConfiguration? = null
    var selectors: SelectorsConfiguration? = null // must be set
    var jsonAuthor: JsonAuthor? = null
    var enabled: Boolean = true
}

class Parameters : EmbeddedRealmObject {
    var key: String = ""
    var fieldType: String = "" // text field, checkbox or message

    // insert placeholder for text field
    // insert default value (on/off) for checkbox
    // insert text to be shown for message
    var data: String = ""
}

class UrlConfiguration : EmbeddedRealmObject {
    var baseUrl: String = "" // ex: https://annas-archive.org
    var searchQuery: String = "" // ex: /search?q=$S where $S is query
    var isbnQuery: String? = null // used for "add a book from barcode" feature (optional)
}

class SelectorsConfiguration : EmbeddedRealmObject {
    var responseType: String = "" // "HTML" or "JSON"
    // selectors should be filled with querySelector or jayway.jsonpath
    var searchSelectors: SearchSelectors? = null
    var bookSelectors: BookSelectors? = null
}

class SearchSelectors : EmbeddedRealmObject {
    // querySelector of selectors will be applied
    // on each result of querySelectorAll(eachBookSelector) ;
    // or use jsonpath if response is json
    var eachBookSelector: String = ""
    var selectors: BookSelectors? = null
    var linkToBook: String = ""
}

class BookSelectors : EmbeddedRealmObject {
    // fill as much information as possible, no information is preferred over wrong information
    var title: String = ""
    var author: AuthorSelectors? = null
    var description: String? = null
    var year: String? = null
    var language: String? = null
    var pages: String? = null // pages count
    var publisher: String? = null
    var edition: String? = null
    var isbn: String? = null
    var imageURL: String? = null

    var downloadMirrors: RealmList<String> = realmListOf()
    var fileSize: String? = null
    // the dot prefix will be automatically removed
    // preferred file format in order: epub, mobi/azw3, cbz, fb2, pdf, txt
    var fileExtension: String? = null
}

class AuthorSelectors : EmbeddedRealmObject {
    var name: String = ""
    var description: String? = null
    var language: String? = null

    var imageURL: String? = null
    // will be fetched automatically but better if set
    // as it avoids having to search the author wikipedia page
    var wikipediaURL: String? = null
}

class JsonAuthor : EmbeddedRealmObject {
    var name: String = ""
    var email: String = ""
    var pgpPublicKey: String = ""
    var requestUpdateUrl: String? = null // ex: mail uri with pre-written message
    var updateUrls: RealmList<String> = realmListOf() // list of mirrors to update the json file
    // markdown text, will be shown in app, max size is 2048 characters
    // it can be anything but it is recommended to include one or multiple contact information
    var customMessage: String? = null
}