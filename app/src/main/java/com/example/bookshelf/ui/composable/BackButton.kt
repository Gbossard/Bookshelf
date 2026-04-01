package com.example.bookshelf.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun BackButton(
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        onClick = onGoBack
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back_24dp),
            contentDescription = stringResource(id = R.string.navigation_back)
        )
    }
}