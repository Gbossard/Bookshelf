package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookshelf.R

@Composable
fun BookDetailsScreen(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    bottomBarState.value = false
    Box(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize(),
    ) {
        LazyColumn {
            item {
                BookDetailsCard()
            }
        }
        BackButton(
            navController = navController,
            modifier = Modifier.align(alignment = Alignment.TopStart)
        )
        BookDetailsButtonToBuy(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BackButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = CircleShape,
        onClick = {
            navController.navigateUp()
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back_24dp),
            contentDescription = stringResource(id = R.string.navigation_back)
        )
    }
}

@Composable
fun BookDetailsCard() {
    Column {
        BookDetailsHeader()
        BookDetailsDescription()
    }
}

@Composable
fun BookDetailsHeader() {
    Box {
        Image(
            painter = painterResource(R.drawable.gaelle_cv),
            contentDescription = stringResource(id = R.string.profile_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .size(450.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}

@Composable
fun BookDetailsDescription(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp)
    ) {
        Text(
            text = stringResource(R.string.book_details_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.book_details_author),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(R.string.book_details_date) ,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = stringResource(R.string.book_details_description_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = stringResource(R.string.book_details_description),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(R.string.book_details_information),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_publisher),
            bodyText = stringResource(R.string.book_details_publisher)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_page_count),
            bodyText = stringResource(R.string.book_details_page_count)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_isbn_10),
            bodyText = stringResource(R.string.book_details_isbn_10)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_isbn_13),
            bodyText = stringResource(R.string.book_details_isbn_13)
        )
    }
}

@Composable
fun RowDetailsInformation(
    titleText: String,
    bodyText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = bodyText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun BookDetailsButtonToBuy(
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { },
    ) {
        Text(
            text = stringResource(R.string.book_details_button_to_buy),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}