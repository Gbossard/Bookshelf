package com.example.bookshelf.ui.composable


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun ParagraphText(
    text: String,
    modifier: Modifier = Modifier
) {
    val paragraphs = getFormattedText(text)
    Text(
        text = paragraphs,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

fun getFormattedText(text: String): AnnotatedString {
    return buildAnnotatedString {
        var remainingText = text

        while (remainingText.isNotEmpty()) {
            val startP = remainingText.indexOf("<p>")
            val startI = remainingText.indexOf("<i>")
            val startB = remainingText.indexOf("<b>")
            val startUl = remainingText.indexOf("<ul>")
            val startLi = remainingText.indexOf("<li>")

            val nextTagIndex = listOf(startP, startI, startB, startUl, startLi)
                .filter { it >= 0 }
                .minOrNull()

            if (nextTagIndex == null) {
                append(remainingText)
                break
            }

            append(remainingText.substring(0, nextTagIndex))

            when {
                startP == nextTagIndex -> {
                    val endP = remainingText.indexOf("</p>", startP)
                    if (endP != -1) {
                        val paragraph = remainingText.substring(startP + 3, endP)
                        append(getFormattedText(paragraph))
                        remainingText = remainingText.substring(endP + 4)
                    }
                }
                startI == nextTagIndex -> {
                    val endI = remainingText.indexOf("</i>", startI)
                    if (endI != -1) {
                        val italicText = remainingText.substring(startI + 3, endI)
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(italicText)
                        }
                        remainingText = remainingText.substring(endI + 4)
                    }
                }
                startB == nextTagIndex -> {
                    val endB = remainingText.indexOf("</b>", startB)
                    if (endB != -1) {
                        val boldText = remainingText.substring(startB + 3, endB)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(boldText)
                        }
                        remainingText = remainingText.substring(endB + 4)
                    }
                }
                startUl == nextTagIndex -> {
                    val endUl = remainingText.indexOf("</ul>", startUl)
                    if (endUl != -1) {
                        remainingText = remainingText.substring(startUl + 4, endUl)
                        append("\n")
                    }
                }
                startLi == nextTagIndex -> {
                    val endLi = remainingText.indexOf("</li>", startLi)
                    if (endLi != -1) {
                        val listItem = remainingText.substring(startLi + 4, endLi)
                        append("• $listItem\n")
                        remainingText = remainingText.substring(endLi + 5)
                    }
                }
            }
        }
    }
}

