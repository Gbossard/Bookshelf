package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R


@Composable
fun BookListScreen(
    bottomBarState: MutableState<Boolean>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    bottomBarState.value = true
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentPadding = contentPadding,
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}