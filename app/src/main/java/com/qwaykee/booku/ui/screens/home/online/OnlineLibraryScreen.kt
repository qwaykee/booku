package com.qwaykee.booku.ui.screens.home.online

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.qwaykee.booku.R

object OnlineLibraryScreen : Tab {
    private fun readResolve(): Any = OnlineLibraryScreen
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.online)
            val icon = painterResource(R.drawable.ic_online)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { OnlineLibraryScreenModel() }
    }
}