package com.example.bookshelf.ui.screens.bookList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.composable.ErrorScreen
import com.example.bookshelf.ui.composable.LoadingScreen


@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    bookshelfUiState: BookshelfUiState,
    onGoDetails: (Book) -> Unit,
    retryAction: () -> Unit,
) {
    when(bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize(), isList = true)
        is BookshelfUiState.Success ->
            BooksGridScreen(
                books = bookshelfUiState.books,
                modifier = modifier,
                onGoDetails = onGoDetails
            )
        is BookshelfUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun BooksGridScreen(
    books: List<Book>,
    onGoDetails: (Book) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize().padding(16.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        items(items = books, key = { it.id }) { book ->
            if (book.volumeInfo.imageLinks?.thumbnail ==  null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable { onGoDetails(book) }
                ) {
                    Image(
                        modifier = modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        painter = painterResource(R.drawable.book),
                        contentScale = ContentScale.Crop,
                        alpha = 0.3f,
                        contentDescription = stringResource(R.string.book_unknown_image)
                    )
                    Text(
                        text = book.volumeInfo.title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(book.volumeInfo.imageLinks.httpsSmallThumbnail)
                        .crossfade(true)
                        .fallback(R.drawable.book)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.book_image),
                    modifier = Modifier.fillMaxWidth().clickable { onGoDetails(book) }.clip(RoundedCornerShape(24.dp))
                )
            }
        }
    }
}