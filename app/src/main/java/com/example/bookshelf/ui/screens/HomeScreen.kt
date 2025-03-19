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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.ui.composable.BoxImage

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            HomeHeader()
            HomeSearch()
            HomeCategories()
            HomePopularBooks()
        }
    }
}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.hello_name),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(R.drawable.gaelle_cv),
            contentDescription = stringResource(id = R.string.profile_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearch(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = stringResource(R.string.search),
            onQueryChange = { },
            onSearch = { },
            active = false,
            onActiveChange = {},
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        ) { }
    }
}

@Composable
fun HomeCategories(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxImage(
                widthSize = 65,
                heightSize = 65,
                roundedCornerShape = 20
            )
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxImage(
                widthSize = 65,
                heightSize = 65,
                roundedCornerShape = 20
            )
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxImage(
                widthSize = 65,
                heightSize = 65,
                roundedCornerShape = 20
            )
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxImage(
                widthSize = 65,
                heightSize = 65,
                roundedCornerShape = 20
            )
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun HomePopularBooks(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 0.dp, bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.latest_releases),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
            item {
                BoxImage(
                    widthSize = 250,
                    heightSize = 350,
                    roundedCornerShape = 28
                )
            }
        }
    }
}