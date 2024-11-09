package com.qwaykee.booku.data.manager

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qwaykee.booku.data.models.Book
import java.io.File

interface FileManager {
    fun retrieveMetadata(file: File): Book

    @Composable
    fun render(context: Context, file: File, parameters: RenderingParameters)

    fun compress(file: File)
}

class RenderingParameters {
    val fontSize: Dp = 16.dp
}
