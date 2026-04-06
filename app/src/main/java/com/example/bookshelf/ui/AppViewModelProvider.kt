package com.example.bookshelf.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.ui.screens.bookDetails.BookshelfDetailsViewModel
import com.example.bookshelf.ui.screens.bookList.BookshelfViewModel
import com.example.bookshelf.ui.screens.favorites.FavoriteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            BookshelfViewModel(bookshelfApplication().container.bookshelfRepository)
        }
        initializer {
            BookshelfDetailsViewModel(bookshelfApplication().container.bookshelfRepository)
        }
        initializer {
            FavoriteViewModel(bookshelfApplication().container.bookshelfRepository)
        }
    }
}

fun CreationExtras.bookshelfApplication() :BookshelfApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)