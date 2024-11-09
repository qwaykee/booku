package com.qwaykee.booku.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

@Suppress("unused")
class Author : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var name: String = ""
    var description: String = ""
    var language: String = ""
    var books: RealmList<Book> = realmListOf()

    var imageURL: String = ""
    var imagePath: String = ""
    var wikipediaURL: String = ""
}