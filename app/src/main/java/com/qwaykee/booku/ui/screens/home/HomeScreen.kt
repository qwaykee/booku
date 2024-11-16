package com.qwaykee.booku.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.qwaykee.booku.R
import com.qwaykee.booku.data.preferences.PreferencesKeys
import com.qwaykee.booku.data.preferences.rememberPreference
import com.qwaykee.booku.ui.screens.home.feed.FeedScreen
import com.qwaykee.booku.ui.screens.home.library.LibraryScreen
import com.qwaykee.booku.ui.screens.home.online.OnlineLibraryScreen
import com.qwaykee.booku.ui.screens.settings.SettingsScreen

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(LibraryScreen) {
            Scaffold (
                topBar = { TopBar() },
                bottomBar = { BottomBar() }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    CurrentTab()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        val navigator = LocalNavigator.currentOrThrow.parent
        val tabNavigator = LocalTabNavigator.current

        LargeTopAppBar(
            title = {
                Text(text = tabNavigator.current.options.title)
            },
            navigationIcon = {
                IconButton(onClick = { navigator?.push(SettingsScreen()) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    // TODO: Handle search button click
                    when (tabNavigator.current) {
                        is FeedScreen -> {}
                        is LibraryScreen -> {}
                        is OnlineLibraryScreen -> {}
                        else -> {}
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = when (tabNavigator.current) {
                            is FeedScreen -> stringResource(R.string.search_feed)
                            is LibraryScreen -> stringResource(R.string.search_library)
                            is OnlineLibraryScreen -> stringResource(R.string.search_online)
                            else -> stringResource(R.string.search)
                        }
                    )
                }

                if (tabNavigator.current != LibraryScreen) {
                    // TODO: Handle refresh button click
                    IconButton(onClick = {
                        when (tabNavigator.current) {
                            is FeedScreen -> {}
                            is OnlineLibraryScreen -> {}
                            else -> {}
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = when (tabNavigator.current) {
                                is FeedScreen -> stringResource(R.string.refresh_feed)
                                is OnlineLibraryScreen -> stringResource(R.string.refresh_online)
                                else -> stringResource(R.string.refresh)
                            }
                        )
                    }
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

    @Composable
    fun BottomBar() {
        val viewModel = rememberScreenModel { HomeScreenModel() }

        val internetEnabled = rememberPreference(
            LocalContext.current,
            PreferencesKeys.InternetEnabled.key,
            false
        )

        val feedsCount by viewModel.feedsCount.collectAsState()
        val onlineLibrariesCount by viewModel.onlineLibrariesCount.collectAsState()

        if (internetEnabled && (feedsCount > 0 || onlineLibrariesCount > 0)) {
            NavigationBar (containerColor = MaterialTheme.colorScheme.background) {
                Spacer(modifier = Modifier.weight(1f))
                if (feedsCount > 0) {
                    TabNavigationItem(FeedScreen)
                }
                TabNavigationItem(LibraryScreen)
                if (onlineLibrariesCount > 0) {
                    TabNavigationItem(OnlineLibraryScreen)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current
        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
        )
    }
}