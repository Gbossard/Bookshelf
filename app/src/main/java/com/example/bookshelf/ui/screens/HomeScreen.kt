package com.example.bookshelf.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Text("Écran d'Accueil", modifier = Modifier.fillMaxSize())
}

@Composable
fun SettingsScreen() {
    Text("Écran des Paramètres", modifier = Modifier.fillMaxSize())
}