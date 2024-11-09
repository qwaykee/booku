package com.qwaykee.booku

import android.app.Application
import com.qwaykee.booku.data.models.Author
import com.qwaykee.booku.data.models.AuthorSelectors
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.models.BookSelectors
import com.qwaykee.booku.data.models.Feed
import com.qwaykee.booku.data.models.FeedEntry
import com.qwaykee.booku.data.models.OnlineLibrary
import com.qwaykee.booku.data.models.Collection
import com.qwaykee.booku.data.models.JsonAuthor
import com.qwaykee.booku.data.models.Parameters
import com.qwaykee.booku.data.models.SearchSelectors
import com.qwaykee.booku.data.models.SelectorsConfiguration
import com.qwaykee.booku.data.models.UrlConfiguration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class Booku : Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(
                schema = setOf(
                    Book::class,
                    Feed::class,
                    FeedEntry::class,
                    Author::class,
                    Collection::class,

                    OnlineLibrary::class,
                    Parameters::class,
                    UrlConfiguration::class,
                    SelectorsConfiguration::class,
                    SearchSelectors::class,
                    BookSelectors::class,
                    AuthorSelectors::class,
                    JsonAuthor::class
                )
            )
            .schemaVersion(3) // TODO: Remove
            // .deleteRealmIfMigrationNeeded() // TODO: Remove
            .inMemory() // TODO: Remove
            .build()

        realm = Realm.open(config)
    }
}