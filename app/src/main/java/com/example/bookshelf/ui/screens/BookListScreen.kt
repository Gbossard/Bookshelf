package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.composable.ErrorScreen
import com.example.bookshelf.ui.composable.LoadingScreen


@Composable
fun BookListScreen(
    onGoDetails: (Book) -> Unit,
    retryAction: () -> Unit,
    bottomBarState: MutableState<Boolean>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
    val bookshelfUiState by bookshelfViewModel.bookshelfUiState.collectAsState()

    bottomBarState.value = true

    LaunchedEffect(Unit) {
        bookshelfViewModel.getBooks()
    }

    when(bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookshelfUiState.Success ->
            BooksGridScreen(
                books = (bookshelfUiState as BookshelfUiState.Success).books,
                contentPadding = contentPadding,
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
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentPadding = contentPadding,
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        items(items = books, key = { book -> book.id}) { book ->
            if (book.volumeInfo.imageLinks?.thumbnail ==  null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable { onGoDetails(book) }
                ) {
                    Image(
                        modifier = modifier
                            .size(275.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        painter = painterResource(R.drawable.book),
                        contentScale = ContentScale.Crop,
                        alpha = 0.3f,
                        contentDescription = stringResource(R.string.loading_failed)
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
                        .data(book.volumeInfo.imageLinks.httpsThumbnail)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.book_details_description),
                    modifier = Modifier.fillMaxWidth().clickable { onGoDetails(book) }.clip(RoundedCornerShape(24.dp))
                )
            }
        }
    }
}