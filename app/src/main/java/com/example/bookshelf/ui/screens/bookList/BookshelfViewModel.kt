package com.example.bookshelf.ui.screens.bookList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.repository.BookshelfRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "BookshelfViewModel"
sealed interface BookshelfUiState {
    data class Success(val books: List<BookEntity>) : BookshelfUiState
    object Error: BookshelfUiState
    object Loading: BookshelfUiState
}

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {

    private val _searchRawUiState = MutableStateFlow("travel+newZealand")

    @OptIn( ExperimentalCoroutinesApi::class, FlowPreview::class)
    val uiState: StateFlow<BookshelfUiState> = _searchRawUiState
        .debounce(300)
        .flatMapLatest { query ->
            flow {
                val localBooks = bookshelfRepository.getAllBooks().firstOrNull()

                if (localBooks.isNullOrEmpty()) {
                    emit(BookshelfUiState.Loading)
                }

                try {
                    bookshelfRepository.refreshBooks(query)
                } catch (error: IOException) {
                    Log.e(TAG, "Network error refreshBooks", error)
                    emit(BookshelfUiState.Error)
                } catch (error: HttpException) {
                    Log.e(TAG, "HTTP error refreshBooks", error)
                    emit(BookshelfUiState.Error)
                } catch (error: Exception) {
                    Log.e(TAG, "Unexpected error refreshBooks", error)
                    emit(BookshelfUiState.Error)
                }

                emitAll(
                    bookshelfRepository.getAllBooks().map { books ->
                        if (books.isEmpty()) BookshelfUiState.Error
                        else BookshelfUiState.Success(books)

                    }
                )
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookshelfUiState.Loading
        )

    fun updateSearch(query: String = "travel+newZealand") {
        if (_searchRawUiState.value == query) return
        _searchRawUiState.value = query
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