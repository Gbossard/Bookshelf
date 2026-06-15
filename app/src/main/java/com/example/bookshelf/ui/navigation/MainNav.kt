package com.example.bookshelf.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.screens.bookDetails.BookDetailsScreen
import com.example.bookshelf.ui.screens.bookDetails.BookshelfDetailsViewModel
import com.example.bookshelf.ui.screens.bookList.BookListScreen
import com.example.bookshelf.ui.screens.bookList.BookshelfViewModel
import com.example.bookshelf.ui.screens.favorites.FavoriteScreen
import com.example.bookshelf.ui.screens.favorites.FavoriteViewModel
import com.example.bookshelf.ui.screens.search.SearchScreen
import com.example.bookshelf.ui.screens.search.SearchViewModel

@Composable
fun MainNav() {
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(navBackStackEntry) {
        bottomBarState.value = navBackStackEntry?.destination?.route != Screen.Details.routes
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (bottomBarState.value) {
                BookshelfBottomAppBar(navController)
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.routes,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Screen.Home.routes) {
                HomeScreen(
                    onSearchClick = {
                        navController.navigate(Screen.Search.routes)
                    }
                )
            }
            composable(Screen.Search.routes) {
                val searchViewModel: SearchViewModel = hiltViewModel()
                val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
                val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()

                SearchScreen(
                    query = searchQuery,
                    onQueryChange = { newQuery ->
                        searchViewModel.onQueryChange(newQuery)
                    },
                    searchUiState = searchUiState,
                    onBack = {
                        navController.popBackStack()
                    },
                    onGoDetails = { book ->
                        navController.navigate(Screen.Details.createRoute(book.id))
                    },
                    onClearQuery = {
                        searchViewModel.onClearQuery()
                    },
                    onClickFavorite = {
                        searchViewModel.toggleFavorite(it)
                    },
                    events = searchViewModel.events,
                    snackbarHostState = snackbarHostState
                )
            }
            composable(Screen.BooksCategories.routes) {
                val bookshelfViewModel: BookshelfViewModel = hiltViewModel()
                val bookshelfUiState by bookshelfViewModel.uiState.collectAsStateWithLifecycle()

                BookListScreen(
                    bookshelfUiState = bookshelfUiState,
                    onGoDetails = { book ->
                        navController.navigate(Screen.Details.createRoute(book.id))
                    },
                    loadBooks = {
                        bookshelfViewModel.updateSearch()
                    },
                    onClickFavorite = { book ->
                        bookshelfViewModel.toggleFavorite(book)
                    }
                )
            }
            composable(
                route = Screen.Details.routes,
                arguments = listOf(navArgument("selectedBookId") { type = NavType.StringType })
            ) { backStackEntry ->
                val selectedBookId = backStackEntry.arguments?.getString("selectedBookId") ?: ""

                val bookshelfDetailsViewModel: BookshelfDetailsViewModel = hiltViewModel()
                val bookshelfDetailsUiState by bookshelfDetailsViewModel.uiState.collectAsStateWithLifecycle()

                BookDetailsScreen(
                    bookshelfDetailsUiState = bookshelfDetailsUiState,
                    onGoBack = {
                        navController.popBackStack()
                    },
                    selectedBookId = selectedBookId,
                    loadBook = { id ->
                        bookshelfDetailsViewModel.getBook(id)
                    },
                    retryAction = {
                        bookshelfDetailsViewModel.getBook(selectedBookId)
                    },
                    onClickFavorite = { book ->
                        bookshelfDetailsViewModel.toggleFavorite(book)
                    }
                )
            }
            composable(Screen.Favorites.routes) {
                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                val favoriteUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

                FavoriteScreen(
                    favoriteUiState = favoriteUiState,
                    onClickFavorite = { book ->
                        favoriteViewModel.toggleFavorite(book)
                    },
                    onGoDetails = { book ->
                        navController.navigate(Screen.Details.createRoute(book.id))
                    },
                    onGoHome = {
                        navController.navigate(Screen.Home.routes)
                    }
                )
            }
        }
    }
}

@Composable
fun BookshelfBottomAppBar(
    navController: NavController
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_home_24dp), contentDescription = stringResource(R.string.home)) },
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.routes) {
                    popUpTo(Screen.Home.routes) { inclusive = true }
                }
            }
        )
//        NavigationBarItem(
//            icon = { Icon(painter = painterResource(R.drawable.ic_flight), contentDescription = stringResource(R.string.flight)) },
//            selected = false,
//            onClick = {
//                navController.navigate(Screen.BooksCategories.routes) {
//                    popUpTo(Screen.BooksCategories.routes) { inclusive = true}
//                }
//            }
//        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_favorite_24dp), contentDescription = stringResource(R.string.favorites)) },
            selected = false,
            onClick = {
                navController.navigate(Screen.Favorites.routes) {
                    popUpTo(Screen.Favorites.routes) { inclusive = true }
                }
            }
        )
    }
}