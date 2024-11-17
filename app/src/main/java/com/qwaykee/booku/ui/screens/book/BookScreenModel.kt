package com.qwaykee.booku.ui.screens.book

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.data.network.NetworkHelper
import com.qwaykee.booku.data.repository.LocalRepository
import com.qwaykee.booku.data.repository.OnlineLibraryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

data class BookScreenModel(val bookId: ObjectId) : ScreenModel {
    private val localRepository = LocalRepository()
    private val onlineLibraryRepository = OnlineLibraryRepository(NetworkHelper(), null)
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    val book: StateFlow<Book?> = localRepository
        .getBook(bookId)
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            Book()
        )

    fun addToLibrary() {
        screenModelScope.launch {
            book.value?.let { book ->
                val file = onlineLibraryRepository.getBookFile(book)
                file?.let {
                    book.filePath = file.path
                    localRepository.updateBook(book)
                } ?: run {
                    _snackbarMessage.emit("Error while fetching book") // TODO: I18n
                }
            }
        }
    }

    fun toggleFavorite() {
        screenModelScope.launch {
            book.value?.let {
                localRepository.toggleFavorite(it)
            }
        }
    }
}