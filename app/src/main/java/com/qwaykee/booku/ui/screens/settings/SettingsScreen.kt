package com.qwaykee.booku.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
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
                ClickableText(
                    onclick = { navigator.push(ReaderSettingsScreen()) },
                    title = stringResource(R.string.reader),
                    subtitle = stringResource(R.string.reader_subtitle),
                    icon = painterResource(R.drawable.ic_reader)
                )

                ClickableText(
                    onclick = { navigator.push(LibrarySettingsScreen()) },
                    title = stringResource(R.string.library),
                    subtitle = stringResource(R.string.library_subtitle),
                    icon = painterResource(R.drawable.ic_library)
                )

                ClickableText(
                    onclick = { navigator.push(ThemeSettingsScreen()) },
                    title = stringResource(R.string.theme),
                    subtitle = stringResource(R.string.theme_subtitle),
                    icon = painterResource(R.drawable.ic_theme)
                )

                ClickableText(
                    onclick = { navigator.push(ManagementSettingsScreen()) },
                    title = stringResource(R.string.management),
                    subtitle = stringResource(R.string.management_subtitle),
                    icon = painterResource(R.drawable.ic_management)
                )

                ClickableText(
                    onclick = { navigator.push(InternetSettingsScreen()) },
                    title = stringResource(R.string.internet),
                    subtitle = stringResource(R.string.internet_subtitle),
                    icon = painterResource(R.drawable.ic_online)
                )

                ClickableText(
                    onclick = { navigator.push(LanguageSettingsScreen()) },
                    title = stringResource(R.string.language),
                    subtitle = currentLanguageDisplay,
                    icon = painterResource(R.drawable.ic_language)
                )

                ClickableText(
                    onclick = { navigator.push(AboutSettingsScreen()) },
                    title = stringResource(R.string.about),
                    subtitle = stringResource(R.string.about_subtitle),
                    icon = painterResource(R.drawable.ic_about)
                )
            }
        }
    }

    @Composable
    fun ClickableText(
        modifier: Modifier = Modifier,
        onclick: () -> Unit,
        title: String,
        subtitle: String,
        icon: Painter
    ) {
        Row(
            modifier = modifier
                .clickable(onClick = onclick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .size(32.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}