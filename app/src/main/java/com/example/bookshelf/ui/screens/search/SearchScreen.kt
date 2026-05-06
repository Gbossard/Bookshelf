package com.example.bookshelf.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.ui.composable.BooksGrid
import com.example.bookshelf.ui.composable.EmptySearch
import com.example.bookshelf.ui.composable.ErrorScreen
import com.example.bookshelf.ui.composable.LoadingScreen
import com.example.bookshelf.ui.composable.NoResults
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    searchUiState: SearchUiState,
    onBack: () -> Unit,
    onClearQuery: () -> Unit,
    onGoDetails: (BookEntity) -> Unit,
    onClickFavorite: (BookEntity) -> Unit,
    events: SharedFlow<SearchUiEvent>,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SearchUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CustomizableSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = {
                IconButton(onClick = {
                    onClearQuery()
                    onBack()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back_24dp_rounded),
                        contentDescription = stringResource(R.string.navigation_back)
                    )
                }
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        onClearQuery()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24dp),
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            },
            searchUiState = searchUiState,
            onGoDetails = onGoDetails,
            onClickFavorite = onClickFavorite,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    searchUiState: SearchUiState,
    onGoDetails: (BookEntity) -> Unit,
    onClickFavorite: (BookEntity) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = { Icon(painter = painterResource(R.drawable.ic_search_24dp), contentDescription = stringResource(R.string.search_icon)) },
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .focusRequester(focusRequester)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        focusManager.clearFocus()
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            },
            colors = SearchBarDefaults.colors(
                dividerColor = Color.Transparent,
                containerColor = Color.Transparent,
            ),
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            when(searchUiState) {
                is SearchUiState.Empty -> {
                    EmptySearch()
                }
                is SearchUiState.Loading -> {
                    LoadingScreen()
                }
                is SearchUiState.Success -> {
                    BooksGrid(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        books = searchUiState.books,
                        onGoDetails = onGoDetails,
                        onClickFavorite = onClickFavorite
                    )
                }
                is SearchUiState.Error -> {
                    ErrorScreen({  }, modifier = Modifier.fillMaxSize())
                }
                is SearchUiState.NoResults -> {
                    NoResults(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}