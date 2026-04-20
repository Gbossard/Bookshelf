package com.example.bookshelf.ui.screens.bookDetails

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.example.bookshelf.R
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.ui.composable.BackButton
import com.example.bookshelf.ui.composable.ErrorScreen
import com.example.bookshelf.ui.composable.LoadingScreen
import com.example.bookshelf.ui.composable.orUnknown

@Composable
fun BookDetailsScreen(
    modifier: Modifier = Modifier,
    selectedBookId: String,
    bookshelfDetailsUiState: BookshelfDetailsUiState,
    onGoBack: () -> Unit,
    loadBook: (String) -> Unit,
    retryAction: () -> Unit,
    onClickFavorite: (BookEntity) -> Unit
) {

    LaunchedEffect(selectedBookId) {
        loadBook(selectedBookId)
    }

    when(bookshelfDetailsUiState) {
        is BookshelfDetailsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize(), isList = false)
        is BookshelfDetailsUiState.Success ->
            DetailScreen(
                modifier = modifier,
                onGoBack = onGoBack,
                book = bookshelfDetailsUiState.book,
                onClickFavorite = onClickFavorite
            )
        is BookshelfDetailsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onClickFavorite: (BookEntity) -> Unit,
    book: BookEntity
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "header") {
                BookDetailsHeaderImage(
                    imageUri = book.thumbnail,
                    isFavorite = book.isFavorite,
                    onClickFavorite = { onClickFavorite(book) }
                )
            }
            item(key = "title") {
                BookDetailsTitle(title = book.title)
            }
            item(key = "authorsAndDate") {
                BookDetailsAuthorsAndDate(
                    authors = book.authors,
                    publishedDate = book.publishedDate
                )
            }
            item(key = "description") {
                BookDetailsDescription(book.description)
            }
            item(key = "information") {
                BookDetailsInformation(
                    publisher = book.publishers,
                    pageCount = book.pageCount,
                    isbn10 = book.isbn10,
                    isbn13 = book.isbn13
                )
            }
        }
        BackButton(
            onGoBack = onGoBack,
            modifier = Modifier.align(alignment = Alignment.TopStart)
        )
        val buyLink = book.buyLink
        if (!buyLink.isNullOrEmpty()) {
            BookDetailsButtonToBuy(
                buyLink = buyLink,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun BookDetailsHeaderImage(
    modifier: Modifier = Modifier,
    imageUri: String?,
    isFavorite: Boolean,
    onClickFavorite: () -> Unit
) {
    Box(
        modifier = modifier
            .size(450.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .fallback(R.drawable.book)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.book_image),
            modifier = Modifier.fillMaxSize()
        )
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = onClickFavorite
        ) {
            Icon(
                painter = if (isFavorite) painterResource(R.drawable.ic_favorite_fill_24dp) else painterResource(R.drawable.ic_favorite_24dp),
                contentDescription = if (isFavorite) stringResource(id = R.string.delete_favorite_button_text) else stringResource(id = R.string.add_favorite_button_text),
            )
        }
    }
}

@Composable
fun BookDetailsTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displaySmall.copy(
            hyphens = Hyphens.Auto,
            lineBreak = LineBreak.Paragraph,
            fontSize = 32.sp
        ),
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun BookDetailsAuthorsAndDate(
    authors: String?,
    publishedDate: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = authors.orUnknown(R.string.book_details_unknown_author),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = publishedDate.orUnknown(R.string.book_details_unknown_date),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun BookDetailsDescription(
    description: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.book_details_description_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )
        Text(
            text = description.orUnknown(R.string.book_details_description_unknown),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun BookDetailsInformation(
    publisher: String?,
    pageCount: Int?,
    isbn10: String?,
    isbn13: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.book_details_information),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_publisher),
            bodyText = publisher.orUnknown(R.string.book_details_unknown_publisher)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_page_count),
            bodyText = pageCount.toString().orUnknown(R.string.book_details_unknown_page_count)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_isbn_10),
            bodyText = isbn10.orUnknown(R.string.book_details_unknown_isbn_10)
        )
        RowDetailsInformation(
            titleText = stringResource(R.string.book_details_isbn_13),
            bodyText = isbn13.orUnknown(R.string.book_details_unknown_isbn_13)
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
    buyLink: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webIntent = Intent(Intent.ACTION_VIEW, buyLink.toUri())
    ExtendedFloatingActionButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { context.startActivity(webIntent) },
    ) {
        Text(
            text = stringResource(R.string.book_details_button_to_buy),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}