package com.qwaykee.booku.ui.screens.author

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.mongodb.kbson.ObjectId

data class AuthorScreen(val authorId: ObjectId) : Screen {
    @Composable
    override fun Content() {
        // TODO: Finish
        Text("Author page")
    }
}