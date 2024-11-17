package com.qwaykee.booku.ui.screens.collection

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.qwaykee.booku.R
import com.qwaykee.booku.ui.screens.home.common.BookRow
import com.qwaykee.booku.ui.screens.home.common.shapeByIndex
import org.mongodb.kbson.ObjectId

@OptIn(ExperimentalMaterial3Api::class)
data class CollectionScreen(val collectionId: ObjectId) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { CollectionScreenModel(collectionId) }
        val collection by viewModel.collection.collectAsState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        Scaffold (
            topBar = {
                LargeTopAppBar(
                    title = { Text(collection?.name ?: stringResource(R.string.no_name)) },
                    navigationIcon = {
                        IconButton({ navigator.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            LazyColumn (contentPadding = padding) {
                collection?.books?.let { books ->
                    itemsIndexed(
                        items = books,
                        key = { _, book -> book._id.toByteArray() }
                    ) { index, book ->
                        BookRow(
                            book = book,
                            shape = shapeByIndex(index, books.size),
                            navigator = navigator,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}