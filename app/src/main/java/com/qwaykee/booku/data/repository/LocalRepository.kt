package com.qwaykee.booku.data.repository

import com.qwaykee.booku.Booku
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.models.Collection
import com.qwaykee.booku.data.models.Feed
import com.qwaykee.booku.data.models.FeedEntry
import com.qwaykee.booku.data.models.OnlineLibrary
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import org.mongodb.kbson.ObjectId
import java.io.File

@Suppress("unused")
class LocalRepository {
    // PERSONAL NOTES
    // convert to hot flow (StateFlow) to cache results in the viewmodel:
    // val book: StateFlow<Book?> = LocalRepository.getBook(id)
    //        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), Book())
    // retrieve data from the screen:
    // val book by viewModel.book.collectAsState(Book())
    // no need for Dispatchers.IO when using asFlow()

    private val realm: Realm = Booku.realm

    fun countBooks(): Flow<Long> {
        return realm
            .query<Book>()
            .count()
            .asFlow()
    }

    fun getBook(id: ObjectId): Flow<Book?> {
        return realm
            .query<Book>("_id == $0", id)
            .first()
            .asFlow()
            .map { it.obj }
    }

    fun getAllBooks(): Flow<List<Book>> {
        return realm
            .query<Book>()
            .sort("lastReadDate", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    fun getFavoriteBooks(): Flow<List<Book>> {
        return realm
            .query<Book>("addedToFavoriteAt > 0")
            .sort("addedToFavoriteAt", Sort.DESCENDING)
            .asFlow()
            .map { it.list }
    }

    fun getBookFromISBN(isbn: String): Flow<Book?> {
        return realm
            .query<Book>("isbn == $0", isbn)
            .first()
            .asFlow()
            .map { it.obj }
    }

    fun getBookFromSearch(text: String, scopes: List<String>): Flow<List<Book>> {
        // scopes: author, title, description, isbn, year, language, publisher, edition
        // limit scopes and text size as much as possible to increase performances
        // return sorted results based on the number of keyword matches in specified columns
        val keywords = text.split(" ").filter { it.isNotBlank() }

        val query = keywords.joinToString(" OR ") { keyword ->
            scopes.joinToString(" OR ") { scope -> "$scope CONTAINS[c] $0" }
        }

        return realm
            .query<Book>(query, *keywords.toTypedArray())
            .asFlow()
            .map { result ->
                result.list.sortedByDescending { book ->
                    keywords.sumOf { keyword ->
                        scopes.count { scope ->
                            when (scope) {
                                "author" -> book.author?.name!!.contains(keyword, ignoreCase = true)
                                "title" -> book.title.contains(keyword, ignoreCase = true)
                                "description" -> book.description.contains(keyword, ignoreCase = true)
                                "isbn" -> book.isbn.contains(keyword, ignoreCase = true)
                                "year" -> book.year.toString().contains(keyword)
                                "language" -> book.language.contains(keyword, ignoreCase = true)
                                "publisher" -> book.publisher.contains(keyword, ignoreCase = true)
                                "edition" -> book.edition.contains(keyword, ignoreCase = true)
                                else -> false
                            }
                        }
                    }
                }
            }
    }

    suspend fun toggleFavorite(book: Book) {
        realm.write {
            findLatest(book)?.let {
                if (it.addedToFavoriteAt > 0) {
                    it.addedToFavoriteAt = 0
                } else {
                    it.addedToFavoriteAt = System.currentTimeMillis()
                }
            }
        }
    }

    suspend fun addBook(book: Book) {
        realm.write {
            copyToRealm(book)
        }
    }

    suspend fun updateBook(newBook: Book) {
        // new book must have the same primary key as the old book
        realm.write {
            copyToRealm(newBook, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun deleteBook(book: Book) {
        realm.write {
            findLatest(book)
                ?.also { delete(it) }
        }
    }

    fun getCollection(id: ObjectId): Flow<Collection?> {
        return realm
            .query<Collection>("_id == $0", id)
            .first()
            .asFlow()
            .map { it.obj }
    }

    fun getBooksCollection(id: ObjectId): Flow<List<Book>> {
        return realm
            .query<Book>("collection._id == $0", id)
            .find()
            .asFlow()
            .map { it.list }
    }

    fun getAllCollections(): Flow<List<Collection>> {
        return realm
            .query<Collection>()
            .sort("name", Sort.ASCENDING)
            .asFlow()
            .map { it.list }
    }

    suspend fun changeCollection(book: Book, collection: Collection) {
        realm.write {
            findLatest(book)?.let {
                it.collection = collection
            }
        }
    }

    fun getBookFile(book: Book): File? {
        // TODO: Actually read file from disk
        val file = File(book.filePath)
        return if (file.exists()) file else null
    }

    fun countFeeds(): Flow<Long> {
        return realm
            .query<Feed>()
            .count()
            .asFlow()
    }

    suspend fun addFeed(feed: Feed) {
        realm.write {
            copyToRealm(feed)
        }
    }

    fun getFeed(url: String): Flow<Feed?> {
         val localFeed = realm
            .query<Feed>("feedUrl == $0", url)
            .first()
            .asFlow()
            .map { it.obj }

        return localFeed.onEmpty { FeedRepository().getFeed(url).asFlow() }
    }

    fun getAllFeeds(): Flow<List<Feed>> {
        return realm
            .query<Feed>()
            .asFlow()
            .map { it.list }
    }

    suspend fun getFeedEntries(feed: Feed): Flow<List<FeedEntry>> {
        val newEntries = FeedRepository()
            .getEntries(feed)
            .filter { it.publishDate > feed.lastClientUpdateDate }

        realm.write {
            val liveFeed = findLatest(feed)
            liveFeed?.entries?.addAll(newEntries)
            liveFeed?.lastClientUpdateDate = System.currentTimeMillis()
        }

        return realm
            .query<FeedEntry>("feed._id == $0", feed._id)
            .asFlow()
            .map { it.list }
    }

    suspend fun deleteFeed(feed: Feed) {
        realm.write {
            findLatest(feed)
                ?.also { delete(it) }
        }
    }

    fun countOnlineLibraries(): Flow<Long> {
        return realm
            .query<OnlineLibrary>()
            .count()
            .asFlow()
    }
}