package com.qwaykee.booku.ui.screens.settings.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import com.qwaykee.booku.data.preferences.savePreference
import com.qwaykee.booku.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
class LibrarySettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold (
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.library)) },
                    navigationIcon = {
                        IconButton({ navigator.pop() }) {
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
            Column (modifier = Modifier.padding(padding).padding(16.dp)) {
                FileScanSection()
            }
        }
    }

    @Composable
    fun FileScanSection() {
        val context = LocalContext.current
        val scanHiddenFiles = rememberPreference(context, PreferencesKeys.ScanHiddenFiles.key, false)
        val scanBehavior = rememberPreference(context, PreferencesKeys.ScanBehavior.key, "do_not_scan")

        Column {
            Text(
                text = stringResource(R.string.file_scan),
                style = Typography.labelLarge
            )

            SingleSelectionChipGroup(
                items = mapOf(
                    stringResource(R.string.do_not_scan) to "do_not_scan",
                    stringResource(R.string.selected_folders) to "selected_folders",
                    stringResource(R.string.whole_device) to "whole_device"
                ),
                selectedItem = scanBehavior,
                onSelectionChanged = {
                    savePreference(
                        context,
                        PreferencesKeys.ScanBehavior.key,
                        it
                    )
                }
            )

            AnimatedVisibility(scanBehavior == "selected_folders") {
                Button(
                    onClick = {
                        // TODO: Open dialog
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.manage_selected_folders))
                }
            }

            AnimatedVisibility(scanBehavior != "do_not_scan") {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.scan_hidden_files)) },
                    trailingContent = {
                        Switch(
                            onCheckedChange = {
                                savePreference(
                                    context,
                                    PreferencesKeys.ScanHiddenFiles.key,
                                    it
                                )
                            },
                            checked = scanHiddenFiles
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun SingleSelectionChipGroup(
        items: Map<String, String>,
        selectedItem: String?,
        onSelectionChanged: (String) -> Unit
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                FilterChip(
                    selected = selectedItem == item.value,
                    onClick = { onSelectionChanged(item.value) },
                    label = { Text(item.key) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}