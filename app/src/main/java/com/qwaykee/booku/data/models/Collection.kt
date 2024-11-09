package com.qwaykee.booku.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Collection : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()

    var name: String = ""
    var description: String = ""
    var books: RealmList<Book> = realmListOf()
}