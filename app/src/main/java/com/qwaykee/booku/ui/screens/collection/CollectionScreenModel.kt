package com.qwaykee.booku.ui.screens.collection

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.models.Collection
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.mongodb.kbson.ObjectId

data class CollectionScreenModel(val collectionId: ObjectId) : ScreenModel {
    private val localRepository = LocalRepository()

    val collection = localRepository
        .getCollection(collectionId)
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            Collection()
        )
}