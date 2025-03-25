package com.example.bookshelf.ui.screens.bookList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookshelfUiState {
    data class Success(val books: List<Book>) : BookshelfUiState
    object Error: BookshelfUiState
    object Loading: BookshelfUiState
}

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    var selectedBookId by mutableStateOf("")

    init {
        getBooks()
    }

    fun getBooks(query: String = "travel+newZealand") {
        bookshelfUiState = BookshelfUiState.Loading
        viewModelScope.launch {
            try {
                val books = bookshelfRepository.getBooks(query)
                bookshelfUiState = when {
                    books == null -> {
                        BookshelfUiState.Error
                    }
                    books.isEmpty() -> {
                        BookshelfUiState.Success(emptyList())
                    }
                    else -> {
                        BookshelfUiState.Success(books)
                    }
                }
            } catch (e: IOException) {
                BookshelfUiState.Error
            } catch (e: HttpException) {
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}