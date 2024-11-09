@file:Suppress("unused")

package com.qwaykee.booku.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Feed : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()

    var title: String = ""
    var description: String = ""
    var lastClientUpdateDate: Long = 0
    var feedUrl: String = ""
    var websiteUrl: String = ""
    var iconUrl: String = ""
    var entries: RealmList<FeedEntry> = realmListOf()
    var enabled: Boolean = true
}

class FeedEntry : EmbeddedRealmObject {
    var feed: Feed? = null
    var title: String = ""
    var articleUrl: String = ""
    var description: String = ""
    var publishDate: Long = 0
    var updateDate: Long = 0
    var read: Boolean = false
    var content: String = ""
}