package com.example.bookshelf.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.BookDetailsScreen
import com.example.bookshelf.ui.screens.BookListScreen
import com.example.bookshelf.ui.screens.HomeScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookshelfApp() {
    val navController = rememberNavController()
    val bottomBarState = remember { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BookshelfBottomAppBar(navController)
            }
        }
    ) { contentPadding ->
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    bottomBarState = bottomBarState,
                    contentPadding = contentPadding
                )
            }
            composable("settings") {
                BookListScreen(
                    bottomBarState = bottomBarState,
                    contentPadding = contentPadding,
                    navController = navController
                )
            }
            composable("details") {
                BookDetailsScreen(
                    contentPadding = contentPadding,
                    bottomBarState = bottomBarState,
                    navController = navController
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
                navController.navigate("home")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Settings, contentDescription = "Paramètres") },
            selected = false,
            onClick = {
                navController.navigate("settings")
            }
        )
    }
}