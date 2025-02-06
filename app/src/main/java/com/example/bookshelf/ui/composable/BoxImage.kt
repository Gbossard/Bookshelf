package com.example.bookshelf.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun BoxImage(
    widthSize: Int,
    heightSize: Int,
    roundedCornerShape: Int
) {
    Box(
        modifier = Modifier
            .size(widthSize.dp, heightSize.dp)
            .clip(RoundedCornerShape(roundedCornerShape.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.profile_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(widthSize.dp, heightSize.dp)
        )
    }
}