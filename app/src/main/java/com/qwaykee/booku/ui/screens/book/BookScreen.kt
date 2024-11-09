package com.qwaykee.booku.ui.screens.book

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.qwaykee.booku.R
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.ui.screens.author.AuthorScreen
import com.qwaykee.booku.ui.screens.collection.CollectionScreen
import com.qwaykee.booku.ui.screens.reader.ReaderScreen
import org.mongodb.kbson.ObjectId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
data class BookScreen(val bookId: ObjectId) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { BookScreenModel(bookId) }
        val book by viewModel.book.collectAsState(Book())

        book?.let {
            BookDetails(it, viewModel)
        } ?: run {
            LoadingOrError()
        }
    }

    @Composable
    fun BookDetails(book: Book, viewModel: BookScreenModel) {
        val navigator = LocalNavigator.currentOrThrow
        val bookFlow by viewModel.book.collectAsState(Book())

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton({ navigator.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        IconButton({ viewModel.toggleFavorite() }) {
                            Icon(
                                painter = painterResource(
                                    if (bookFlow!!.isFavorite) {
                                        R.drawable.ic_favorite_filled
                                    } else {
                                        R.drawable.ic_favorite
                                    }
                                ),
                                contentDescription = stringResource(R.string.add_to_favorites)
                            )
                        }

                        IconButton({ /* TODO */ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_share),
                                contentDescription = stringResource(R.string.share_book)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                Button({ navigator.push(ReaderScreen(book._id)) }) {
                    Text(stringResource(R.string.read))
                }
            }
        ) { padding ->
            val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
            val isLargeScreen = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

            if (isLargeScreen || isLandscape) {
                BookHorizontalPresentation(book, padding)
            } else {
                BookVerticalPresentation(book, padding)
            }
        }
    }

    @Composable
    fun BookHorizontalPresentation(book: Book, padding: PaddingValues) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 64.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                BookCover(book)
                BookCollection(book)
            }

            Column (modifier = Modifier.padding(start = 32.dp)) {
                BookBanner(book)
                BookDescription(book)

                Spacer(modifier = Modifier.height(32.dp))

                BookInformation(book)
            }
        }
    }

    @Composable
    fun BookVerticalPresentation(book: Book, padding: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            BookCover(book, Modifier.fillMaxWidth())

            BookBanner(book)

            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) { BookCollection(book) }

            Column (modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
                BookDescription(book)

                Spacer(modifier = Modifier.height(32.dp))

                BookInformation(book)
            }
        }
    }

    @Composable
    fun BookCover(book: Book, modifier: Modifier = Modifier) {
        val navigator = LocalNavigator.currentOrThrow

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            // TODO: Add onLongClick modify image
            AsyncImage(
                model = book.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .height(270.dp)
                    .width(180.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_pending),
                error = painterResource(R.drawable.ic_broken_image)
            )

            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 6.dp)
            )

            book.author?.let { author ->
                Text(
                    text = author.name,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { navigator.push(AuthorScreen(author._id)) },
                            onLongClick = { /* TODO: Modify author */ }
                        )
                        .padding(bottom = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: run {
                Text(
                    text = stringResource(R.string.unknown_author),
                    modifier = Modifier
                        .clickable {
                            // TODO: Add an author
                        }
                        .padding(bottom = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    fun BookBanner(book: Book) {
        Row (
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(book.year.toString())
                Text(
                    text = stringResource(R.string.year),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(book.pages.toString())

                Text(
                    text = stringResource(R.string.pages),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(book.language)

                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    fun BookCollection(book: Book) {
        val navigator = LocalNavigator.currentOrThrow

        book.collection?.let { collection ->
            Text(
                text = collection.name,
                modifier = Modifier.combinedClickable (
                    onClick = { navigator.push(CollectionScreen(collection._id)) },
                    onLongClick = { /* TODO: Modify collection */ }
                )
            )
        } ?: run {
            Text(
                text = stringResource(R.string.no_collection),
                modifier = Modifier.clickable {
                    // TODO: Add a collection
                }
            )
        }
    }

    @Composable
    fun BookDescription(book: Book) {
        Column {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleLarge
            )

            Text(book.description)
        }
    }

    @Composable
    fun BookInformation(book: Book) {
        Column {
            Text(
                text = stringResource(R.string.informations),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                listOf(
                    "${book.getReadingProgressionPercentage()}%",
                    book.lastReadDateText()
                ).joinToString(" • ")
            )

            val downloadUrl = listOf(
                book.downloadURLFromHTTP,
                book.downloadURLFromIPFS,
                book.downloadURLFromTorrent
            ).firstOrNull {
                it != null
            } ?: stringResource(R.string.no_download_url)

            Text(
                listOf(
                    book.formattedFileSize(),
                    downloadUrl
                ).joinToString(" • ")
            )

            Text(stringResource(R.string.publisher_formatted, book.publisher))
            Text(stringResource(R.string.edition_formatted, book.edition))
            Text(book.isbn)
        }
    }

    @Composable
    fun LoadingOrError() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.error)) },
                    navigationIcon = {
                        IconButton({ navigator.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)) {
                Text(stringResource(R.string.error_while_fetching_book))
            }
        }
    }
}