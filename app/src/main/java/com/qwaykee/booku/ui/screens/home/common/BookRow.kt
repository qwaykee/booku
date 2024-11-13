package com.qwaykee.booku.ui.screens.home.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.ui.screens.book.BookScreen
import com.qwaykee.booku.ui.screens.reader.ReaderScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookRow(book: Book, shape: Shape, navigator: Navigator, modifier: Modifier = Modifier) {
    val progressPercentage = book.getReadingProgressionPercentage()

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(shape)
            .combinedClickable(
                onClick = { navigator.push(BookScreen(book._id)) },
                onLongClick = { navigator.push(ReaderScreen(book._id)) }
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${book.author!!.name} • ${book.year} • ${progressPercentage}%",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun shapeByIndex(index: Int, size: Int) : Shape {
    return if (size == 1) {
        RoundedCornerShape(16.dp)
    } else {
        when (index) {
            0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            size - 1 -> RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )

            else -> RectangleShape
        }
    }
}