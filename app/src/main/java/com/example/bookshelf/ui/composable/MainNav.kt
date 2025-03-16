package com.example.bookshelf.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.R
import com.example.bookshelf.ui.navigation.Screen
import com.example.bookshelf.ui.screens.BookDetailsScreen
import com.example.bookshelf.ui.screens.BookListScreen
import com.example.bookshelf.ui.screens.BookshelfDetailsViewModel
import com.example.bookshelf.ui.screens.BookshelfViewModel
import com.example.bookshelf.ui.screens.HomeScreen

@Composable
fun MainNav() {
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) }
    val bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
    val bookshelfDetailsViewModel: BookshelfDetailsViewModel = viewModel(factory = BookshelfDetailsViewModel.Factory)

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BookshelfBottomAppBar(navController)
            }
        }
    ) { contentPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.routes) {
            composable(Screen.Home.routes) {
                HomeScreen(
                    bottomBarState = bottomBarState,
                    contentPadding = contentPadding
                )
            }
            composable(Screen.BooksCategories.routes) {
                BookListScreen(
                    bottomBarState = bottomBarState,
                    contentPadding = contentPadding,
                    onGoDetails = { book ->
                        bookshelfViewModel.selectedBookId = book.id
                        navController.navigate(Screen.Details.routes)
                    },
                    retryAction = {

                    }
                )
            }
            composable(Screen.Details.routes) {
                bookshelfDetailsViewModel.getBook(bookshelfViewModel.selectedBookId)
                BookDetailsScreen(
                    viewModel = bookshelfDetailsViewModel,
                    contentPadding = contentPadding,
                    bottomBarState = bottomBarState,
                    onGoBack = {
                        navController.popBackStack()
                    },
                    retryAction = {

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
            icon = { Icon(Icons.Rounded.Home, contentDescription = stringResource(R.string.home)) },
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.routes)
            }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(R.drawable.ic_flight), contentDescription = stringResource(R.string.flight)) },
            selected = false,
            onClick = {
                navController.navigate(Screen.BooksCategories.routes)
            }
        )
    }
}