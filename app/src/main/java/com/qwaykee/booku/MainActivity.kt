package com.qwaykee.booku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import com.qwaykee.booku.data.models.Author
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.models.Feed
import com.qwaykee.booku.ui.screens.home.HomeScreen
import com.qwaykee.booku.ui.theme.BookuTheme
import com.qwaykee.booku.ui.transitions.SwipeFadeTransition
import dev.rlqd.isbn.ISBN
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalVoyagerApi::class)
    @Suppress("UNUSED_VARIABLE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val realm = Booku.realm
        realm.writeBlocking {
            deleteAll()
        }
        val bookCount = realm.query<Book>().count().find()
        if (bookCount == 0L) {
            realm.writeBlocking {
                val book1 = copyToRealm(Book().apply {
                    title = "The Great Gatsby"
                    description = "A novel set in the Roaring Twenties, telling the story of Jay Gatsby."
                    year = 1925
                    language = "English"
                    pages = 180
                    publisher = "Charles Scribner's Sons"
                    edition = "First"
                    isbn = try {
                        ISBN.convertToISBN13("743273567")
                    } catch (e: Exception) {
                        ""
                    }
                    readingProgression = 180 // Example pages read
                    isFavorite = true
                    lastReadDate = System.currentTimeMillis()
                    imagePath = "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg" // Example path
                })

                val author1 = copyToRealm(Author().apply {
                    name = "Random name"
                })

                book1.author = author1

                val book2 = copyToRealm(Book().apply {
                    title = "1984"
                    description = "A dystopian novel that explores the dangers of totalitarianism."
                    year = 1949
                    language = "English"
                    pages = 328
                    publisher = "Secker & Warburg"
                    edition = "First"
                    isbn = try {
                        ISBN.convertToISBN13("451524934")
                    } catch (e: Exception) {
                        ""
                    }
                    readingProgression = 200 // Example pages read
                    isFavorite = false
                    lastReadDate = System.currentTimeMillis()
                    downloadURLFromHTTP = "https://github.com/IDPF/epub3-samples/releases/download/20230704/accessible_epub_3.epub"
                    imagePath = "https://gratisography.com/wp-content/uploads/2024/01/gratisography-cyber-kitty-800x525.jpg" // Example path
                })

                val author2 = copyToRealm(Author().apply {
                    name = "George Orwell"
                })

                book2.author = author2

                val feed1 = copyToRealm(Feed().apply {
                    title = "test"
                    description = "abc"
                    lastClientUpdateDate = 0
                    feedUrl = "https://example.org"
                    iconUrl = ""
                    entries = realmListOf()
                })
            }
        }

        setContent {
            BookuTheme {
                Navigator(HomeScreen()) { navigator ->
                    ScreenTransition(
                        navigator = navigator,
                        defaultTransition = SwipeFadeTransition(),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background) // avoid white flash in dark mode
                            .fillMaxSize() // avoid screen resize which might alter the transition
                    )
                }
            }
        }
    }
}