package com.qwaykee.booku.ui.screens.home.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImage
import com.qwaykee.booku.data.models.Book
import com.qwaykee.booku.ui.screens.book.BookScreen

@Composable
fun BookCard(book: Book, navigator: Navigator) {
    AsyncImage(
        model = book.imagePath,
        contentDescription = book.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .width(96.dp)
            .height(170.dp)
            .clickable(onClick = { navigator.push(BookScreen(book._id)) })
            .clip(MaterialTheme.shapes.medium),
    )
}