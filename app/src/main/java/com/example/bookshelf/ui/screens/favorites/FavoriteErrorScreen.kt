package com.example.bookshelf.ui.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun FavoriteErrorScreen(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.favorite_error), contentDescription = stringResource(
                R.string.empty_favorite_message)
        )
        Text(
            text = stringResource(R.string.empty_favorite_message),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = stringResource(R.string.add_favorite_message),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = retryAction) {
            Text(stringResource(R.string.explore_books_button))
        }
    }
}