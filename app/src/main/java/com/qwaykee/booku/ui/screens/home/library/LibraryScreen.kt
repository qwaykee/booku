package com.qwaykee.booku.ui.screens.home.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import com.qwaykee.booku.R
import com.qwaykee.booku.data.models.Collection
import com.qwaykee.booku.ui.screens.book.BookScreen
import com.qwaykee.booku.ui.screens.home.common.BookRow
import com.qwaykee.booku.ui.screens.home.common.shapeByIndex
import com.qwaykee.booku.ui.screens.reader.ReaderScreen

object LibraryScreen : Tab {
    private fun readResolve(): Any = LibraryScreen
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.library)
            val icon = painterResource(R.drawable.ic_library)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    enum class ReadingProgression { ALL, UNREAD, UNFINISHED, READ }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow.parent!!
        val viewModel = navigator.rememberNavigatorScreenModel { LibraryScreenModel() }

        val books by viewModel.books.collectAsState(emptyList())
        val favoriteBooks by viewModel.favoriteBooks.collectAsState(emptyList())
        val collections by viewModel.collections.collectAsState(emptyList())

        var selectedProgression by remember { mutableStateOf<ReadingProgression?>(null) }
        var selectedCollections by remember { mutableStateOf<List<Collection>>(emptyList()) }

        if (books.isNotEmpty()) {
            Column {
                books.firstOrNull()?.let {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                            .clickable { navigator.push(ReaderScreen(it._id)) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(R.string.continue_reading),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    "${it.title} • ${it.author?.name ?: stringResource(R.string.unknown_author)} • ${it.getReadingProgressionPercentage()}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Image(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        LinearProgressIndicator(
                            progress = {
                                it.getReadingProgressionPercentage().toFloat() / 100
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                LazyRow (modifier = Modifier.padding(16.dp)) {
                    itemsIndexed(
                        items = favoriteBooks,
                        key = { _, book -> book._id.toByteArray() }
                    ) { _, book ->
                        AsyncImage(
                            model = book.imagePath,
                            contentDescription = book.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .width(96.dp)
                                .height(170.dp)
                                .clickable(onClick = { navigator.push(BookScreen(book._id)) }),
                        )
                    }
                }

                Row (modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row {
                        FilterChip(
                            selected = selectedProgression == ReadingProgression.ALL,
                            onClick = { selectedProgression = ReadingProgression.ALL },
                            label = { Text(stringResource(R.string.all)) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        FilterChip(
                            selected = selectedProgression == ReadingProgression.UNREAD,
                            onClick = { selectedProgression = ReadingProgression.UNREAD },
                            label = { Text(stringResource(R.string.unread)) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        FilterChip(
                            selected = selectedProgression == ReadingProgression.UNFINISHED,
                            onClick = { selectedProgression = ReadingProgression.UNFINISHED },
                            label = { Text(stringResource(R.string.unfinished)) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        FilterChip(
                            selected = selectedProgression == ReadingProgression.READ,
                            onClick = { selectedProgression = ReadingProgression.READ },
                            label = { Text(stringResource(R.string.read)) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    // TODO: Test collections
                    if (collections.isNotEmpty()) {
                        VerticalDivider()
                    }
                    Row {
                        collections.forEach { collection ->
                            FilterChip(
                                selected = selectedCollections.contains(collection),
                                onClick = {
                                    selectedCollections = if (selectedCollections.contains(collection)) {
                                        selectedCollections - collection
                                    } else {
                                        selectedCollections + collection
                                    }
                                },
                                label = { Text(collection.name) }
                            )
                        }
                    }
                }

                val filteredBooks = books.filter { book ->
                    val progress = book.getReadingProgressionPercentage()

                    when (selectedProgression) {
                        ReadingProgression.UNREAD -> progress == 0
                        ReadingProgression.UNFINISHED -> progress in 1..99
                        ReadingProgression.READ -> progress == 100
                        else -> true
                    } && (selectedCollections.isEmpty() || selectedCollections.contains(book.collection))
                }

                LazyColumn {
                    itemsIndexed(
                        items = filteredBooks,
                        key = { _, book -> book._id.toByteArray() }
                    ) { index, book ->
                        BookRow(
                            book = book,
                            shape = shapeByIndex(index, filteredBooks.size),
                            navigator = navigator,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        } else {
            Text(
                stringResource(R.string.first_time),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }


}