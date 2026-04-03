package com.example.bookshelf.ui.screens.bookList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.ui.composable.BooksGrid
import com.example.bookshelf.ui.composable.ErrorScreen
import com.example.bookshelf.ui.composable.LoadingScreen

@Composable
fun BookListScreen(
    modifier: Modifier = Modifier,
    bookshelfUiState: BookshelfUiState,
    onGoDetails: (BookEntity) -> Unit,
    loadBooks: () -> Unit,
    onClickFavorite: (BookEntity) -> Unit
) {
    LaunchedEffect(Unit) {
        if (bookshelfUiState !is BookshelfUiState.Success) {
            loadBooks()
        }
    }

    when(bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookshelfUiState.Success ->
            BooksGrid(
                books = bookshelfUiState.books,
                modifier = modifier,
                onGoDetails = onGoDetails,
                onClickFavorite = onClickFavorite
            )
        is BookshelfUiState.Error -> ErrorScreen(loadBooks, modifier = modifier.fillMaxSize())
    }
}