package com.example.bookshelf.ui.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml

@Composable
fun ParagraphText(
    text: String,
    modifier: Modifier = Modifier
) {
    val annotatedString = remember(text) {
        AnnotatedString.fromHtml(text)
    }
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}