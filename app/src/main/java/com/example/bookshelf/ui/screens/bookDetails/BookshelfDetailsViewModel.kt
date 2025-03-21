package com.example.bookshelf.ui.screens.bookDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookshelfDetailsUiState {
    data class Success(val book: Book) : BookshelfDetailsUiState
    object Error: BookshelfDetailsUiState
    object Loading: BookshelfDetailsUiState
}

class BookshelfDetailsViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    private val _bookshelfDetailsUiState = MutableStateFlow<BookshelfDetailsUiState>(
        BookshelfDetailsUiState.Loading)
    val bookshelfDetailsUiState: StateFlow<BookshelfDetailsUiState> = _bookshelfDetailsUiState

    private var currentBookId: String? = null

    fun getBook(id: String) {
        if (currentBookId == id) {
            return
        }

        currentBookId = id
        viewModelScope.launch {
            _bookshelfDetailsUiState.value = try {
                val book = bookshelfRepository.getBook(id)
                if (book == null) {
                    BookshelfDetailsUiState.Error
                } else {
                    BookshelfDetailsUiState.Success(book)
                }
            } catch (e: IOException) {
                BookshelfDetailsUiState.Error
            } catch (e: HttpException) {
                BookshelfDetailsUiState.Error
            }
        }
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