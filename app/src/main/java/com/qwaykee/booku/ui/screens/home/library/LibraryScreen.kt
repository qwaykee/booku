package com.qwaykee.booku.ui.screens.home.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.qwaykee.booku.R
import com.qwaykee.booku.ui.screens.home.common.BookCard
import com.qwaykee.booku.ui.screens.home.common.BookRow
import com.qwaykee.booku.ui.screens.home.common.shapeByIndex
import com.qwaykee.booku.ui.screens.reader.ReaderScreen

class LibraryScreen : Tab {
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

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow.parent!!
        val viewModel = navigator.rememberNavigatorScreenModel { LibraryScreenModel() }
        val books by viewModel.books.collectAsState(emptyList())
        val favoriteBooks by viewModel.favoriteBooks.collectAsState(emptyList())

        if (books.isNotEmpty()) {
            Column {
                viewModel.lastReadBook?.let {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
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
                                    "${it.title} • ${it.author!!.name} • ${it.getReadingProgressionPercentage()}%",
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

                LazyRow {
                    itemsIndexed(favoriteBooks) { _, book ->
                        BookCard(book, navigator)
                    }
                }

                LazyColumn {
                    itemsIndexed(books) { index, book ->
                        BookRow(book, shapeByIndex(index, books.size))
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