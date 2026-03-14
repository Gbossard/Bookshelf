package com.example.bookshelf.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun String?.orUnknown(stringResource: Int): String {
    return this?.takeIf { it.isNotBlank() } ?: stringResource(stringResource)
}