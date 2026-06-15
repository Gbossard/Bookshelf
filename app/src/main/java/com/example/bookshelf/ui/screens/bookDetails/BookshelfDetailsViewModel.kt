package com.example.bookshelf.ui.screens.bookDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.repository.BookshelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "BookshelfDetailsViewModel"

sealed interface BookshelfDetailsUiState {
    data class Success(val book: BookEntity) : BookshelfDetailsUiState
    object Error: BookshelfDetailsUiState
    object Loading: BookshelfDetailsUiState
}

@HiltViewModel
class BookshelfDetailsViewModel @Inject constructor(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {
    private val _selectedBookId = MutableStateFlow<String?>(null)

    @OptIn( ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<BookshelfDetailsUiState> = _selectedBookId
        .filterNotNull()
        .flatMapLatest { id ->
            flow {
                val localBook = bookshelfRepository.getBookByIdFlow(id).firstOrNull()
                if (localBook == null) {
                    emit(BookshelfDetailsUiState.Loading)
                }
                try {
                    bookshelfRepository.getBookById(id)
                } catch (error: IOException) {
                    Log.e(TAG, "Network error getBookById", error)
                    emit(BookshelfDetailsUiState.Error)
                } catch (error: HttpException) {
                    Log.e(TAG, "HTTP error getBookById", error)
                    emit(BookshelfDetailsUiState.Error)
                } catch (error: Exception) {
                    Log.e(TAG, "Error getBookById", error)
                    emit(BookshelfDetailsUiState.Error)
                }

                emitAll(
                    bookshelfRepository.getBookByIdFlow(id).map { book ->
                        if (book == null) BookshelfDetailsUiState.Error
                        else BookshelfDetailsUiState.Success(book)
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BookshelfDetailsUiState.Loading
        )

    fun getBook(id: String) {
        if (_selectedBookId.value == id) return
        _selectedBookId.value = id
    }

    fun toggleFavorite(book: BookEntity) {
        viewModelScope.launch {
            bookshelfRepository.toggleFavorite(book)
        }
    }
}