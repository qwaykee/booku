package com.qwaykee.booku.ui.screens.reader

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.qwaykee.booku.data.manager.EpubManager
import com.qwaykee.booku.data.manager.RenderingParameters
import org.mongodb.kbson.BsonObjectId

data class ReaderScreen(val bookId: BsonObjectId) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { ReaderScreenModel(bookId) }
        val bookFile = viewModel.file

        bookFile?.also {
            EpubManager().render(LocalContext.current, it, RenderingParameters())
        }?:run {
            Text("COULDN'T FETCH EPUB FILE")
        }
    }
}