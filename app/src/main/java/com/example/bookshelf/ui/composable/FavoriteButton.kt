package com.example.bookshelf.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClickFavorite: () -> Unit
) {
    FilledIconButton (
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(8.dp),
        onClick = onClickFavorite
    ) {
        Icon(
            painter = if (isFavorite) painterResource(R.drawable.ic_favorite_fill_24dp) else painterResource(R.drawable.ic_favorite_24dp),
            contentDescription = if (isFavorite) stringResource(id = R.string.delete_favorite_button_text) else stringResource(id = R.string.add_favorite_button_text)
        )
    }
}