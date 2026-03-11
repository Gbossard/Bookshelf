package com.example.bookshelf.ui.screens.bookDetails

import android.util.Log
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

private const val TAG = "BookshelfDetailsViewModel"

sealed interface BookshelfDetailsUiState {
    data class Success(val book: Book) : BookshelfDetailsUiState
    object Error: BookshelfDetailsUiState
    object Loading: BookshelfDetailsUiState
}

class BookshelfDetailsViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    var bookshelfDetailsUiState: BookshelfDetailsUiState by mutableStateOf(BookshelfDetailsUiState.Loading)
        private set

    private var currentBookId: String? = null

    fun getBook(id: String) {
        if (currentBookId == id) {
            return
        }

        currentBookId = id
        bookshelfDetailsUiState = BookshelfDetailsUiState.Loading
        viewModelScope.launch {
            try {
                val book = bookshelfRepository.getBook(id)
                bookshelfDetailsUiState = when {
                    book == null -> {
                        BookshelfDetailsUiState.Error
                    }
                    else -> {
                        BookshelfDetailsUiState.Success(book)
                    }
                }
            } catch (error: IOException) {
                Log.e(TAG, "Error in getBook: ${error.message}", error)
                bookshelfDetailsUiState = BookshelfDetailsUiState.Error
            } catch (error: HttpException) {
                Log.e(TAG, "Error in getBook: ${error.message}", error)
                bookshelfDetailsUiState = BookshelfDetailsUiState.Error
            }
        }
    }

    fun retryGetBook(id: String) {
        Log.d(TAG, "Retrying to fetch book with id: $id")
        bookshelfDetailsUiState = BookshelfDetailsUiState.Loading
        currentBookId = null
        getBook(id)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfDetailsViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}