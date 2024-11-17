package com.qwaykee.booku.ui.screens.favorites

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoritesScreenModel : ScreenModel {
    val localRepository = LocalRepository()

    val favoriteBooks = localRepository
        .getFavoriteBooks()
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )
}