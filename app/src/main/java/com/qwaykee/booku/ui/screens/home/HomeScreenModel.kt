package com.qwaykee.booku.ui.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.qwaykee.booku.data.repository.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeScreenModel : ScreenModel {
    private val localRepository = LocalRepository()

    val feedsCount = localRepository
        .countFeeds()
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            0
        )

    val onlineLibrariesCount = localRepository
        .countOnlineLibraries()
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(),
            0
        )
}