package com.example.bookshelf.ui.screens.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.ui.composable.BooksGrid
import com.example.bookshelf.ui.composable.LoadingScreen

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    favoriteUiState: FavoriteUiState,
    onClickFavorite: (BookEntity) -> Unit,
    onGoDetails: (BookEntity) -> Unit,
    onGoHome: () -> Unit
) {
    when(favoriteUiState) {
        is FavoriteUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is FavoriteUiState.Success ->
            if (favoriteUiState.favorites.isEmpty()) {
                FavoriteErrorScreen(
                    modifier = modifier.fillMaxSize(),
                    retryAction = onGoHome
                )
            } else {
                BooksGrid(
                    books = favoriteUiState.favorites,
                    modifier = modifier,
                    onGoDetails = onGoDetails,
                    onClickFavorite = onClickFavorite,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.favorite_title),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        is FavoriteUiState.Error -> FavoriteErrorScreen(
            modifier = modifier.fillMaxSize(),
            retryAction = onGoHome
        )
    }
}