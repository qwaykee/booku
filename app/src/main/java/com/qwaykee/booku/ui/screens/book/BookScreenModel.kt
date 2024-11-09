package com.qwaykee.booku.ui.screens.book

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

data class BookScreenModel(val bookId: ObjectId) : ScreenModel {
    val book: StateFlow<Book?> = LocalRepository()
        .getBook(bookId)
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            Book()
        )

    fun toggleFavorite() {
        screenModelScope.launch {
            book.value?.let {
                LocalRepository().toggleFavorite(it)
            }
        }
    }
}