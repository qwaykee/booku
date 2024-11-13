package com.qwaykee.booku.ui.screens.settings.internet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.qwaykee.booku.R
import com.qwaykee.booku.data.preferences.PreferencesKeys
import com.qwaykee.booku.data.preferences.rememberPreference
import com.qwaykee.booku.data.preferences.savePreference

@OptIn(ExperimentalMaterial3Api::class)
class InternetSettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        val internetEnabled = rememberPreference(
            context,
            PreferencesKeys.InternetEnabled.key,
            true
        )

        Scaffold (
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.internet)) },
                    navigationIcon = {
                        IconButton({ navigator.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column (modifier = Modifier.padding(padding)) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.enable_internet)) },
                    trailingContent = {
                        Switch(
                            checked = internetEnabled,
                            onCheckedChange = {
                                savePreference(
                                    context,
                                    PreferencesKeys.InternetEnabled.key,
                                    it
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}