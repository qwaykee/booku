package com.qwaykee.booku.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.qwaykee.booku.R
import com.qwaykee.booku.data.preferences.PreferencesKeys
import com.qwaykee.booku.data.preferences.rememberPreference
import com.qwaykee.booku.ui.screens.settings.about.AboutSettingsScreen
import com.qwaykee.booku.ui.screens.settings.internet.InternetSettingsScreen
import com.qwaykee.booku.ui.screens.settings.language.LanguageSettingsScreen
import com.qwaykee.booku.ui.screens.settings.library.LibrarySettingsScreen
import com.qwaykee.booku.ui.screens.settings.management.ManagementSettingsScreen
import com.qwaykee.booku.ui.screens.settings.reader.ReaderSettingsScreen
import com.qwaykee.booku.ui.screens.settings.theme.ThemeSettingsScreen
import com.qwaykee.booku.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val currentLanguageDisplay = rememberPreference(
            context = LocalContext.current,
            key = PreferencesKeys.LanguageDisplay.key,
            defaultValue = "Language"
        )

        Scaffold (
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.reader),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.reader_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_reader),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(ReaderSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.library),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.library_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_library),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(LibrarySettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.theme),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.theme_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_theme),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(ThemeSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.management),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.management_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_management),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(ManagementSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.internet),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.internet_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_online),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(InternetSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.language),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(currentLanguageDisplay) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_language),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(LanguageSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.about),
                            style = Typography.titleMedium
                        )
                    },
                    supportingContent = { Text(stringResource(R.string.about_subtitle)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_about),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { navigator.push(AboutSettingsScreen()) }
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}