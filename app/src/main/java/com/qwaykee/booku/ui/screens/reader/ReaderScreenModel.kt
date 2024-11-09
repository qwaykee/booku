package com.qwaykee.booku.ui.screens.reader

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import java.io.File

data class ReaderScreenModel(val bookId: BsonObjectId) : ScreenModel {
    private val localRepository = LocalRepository()

    var file: File? = null

    init {
        screenModelScope.launch {
            val book = localRepository
                .getBook(bookId)
                .collect {
                    file = LocalRepository().getBookFile(it!!)
                }
        }
    }
}