package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        HomeHeader()
        HomeSearch()
    }

}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.hello_name),
            style = MaterialTheme.typography.displaySmall
        )
        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(R.drawable.gaelle_cv),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearch(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        SearchBar(
            query = stringResource(R.string.search),
            onQueryChange = { },
            onSearch = { },
            active = false,
            onActiveChange = {},
            trailingIcon = {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(100)).background(MaterialTheme.colorScheme.primary)
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
fun SettingsScreen() {
    Text("Écran des Paramètres", modifier = Modifier.fillMaxSize())
}