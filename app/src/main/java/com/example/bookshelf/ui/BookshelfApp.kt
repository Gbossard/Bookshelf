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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.screens.SettingsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookshelfApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BookshelfBottomAppBar(navController)
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen()
            }
            composable("settings") {
                SettingsScreen()
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