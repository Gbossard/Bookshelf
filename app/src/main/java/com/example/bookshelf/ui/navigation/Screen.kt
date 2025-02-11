package com.example.bookshelf.ui.navigation

sealed class Screen(val routes: String) {
    data object Home: Screen("/home")
    data object BooksCategories: Screen("/booksCategories")
    data object Details: Screen("/details")
}