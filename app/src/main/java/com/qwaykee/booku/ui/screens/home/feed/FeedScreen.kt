package com.qwaykee.booku.ui.screens.home.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.qwaykee.booku.R

class FeedScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.feed)
            val icon = painterResource(R.drawable.ic_feed)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { FeedScreenModel() }
    }
}