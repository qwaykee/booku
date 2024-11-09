package com.qwaykee.booku.ui.screens.settings.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import com.qwaykee.booku.R

class LibrarySettingsScreen : Screen {
    @Composable
    override fun Content() {
        // TODO: Finish
        Column {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.folders_to_scan),
                    style = MaterialTheme.typography.labelMedium
                )

                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_folder)
                    )
                }
            }
        }
    }
}