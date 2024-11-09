package com.qwaykee.booku.ui.screens.home.library

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LibraryScreenModel : ScreenModel {
    private val localRepository = LocalRepository()

    val books = localRepository
        .getAllBooks()
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val favoriteBooks = localRepository
        .getFavoriteBooks()
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val lastReadBook = if (books.value.isNotEmpty()) { books.value[0] } else { null }
}