package com.example.bookshelf.ui.screens.favorites

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "FavoriteViewModel"
sealed interface FavoriteUiState {
    data class Success(val favorites: List<BookEntity>) : FavoriteUiState
    object Error: FavoriteUiState
    object Loading: FavoriteUiState
}

class FavoriteViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    val uiState: StateFlow<FavoriteUiState> = bookshelfRepository.getAllFavorites()
        .map<List<BookEntity>, FavoriteUiState> { favorites ->
            FavoriteUiState.Success(favorites)
        }
        .onStart { emit(FavoriteUiState.Loading) }
        .catch { error ->
            Log.e(TAG, "Error to get favorites", error)
            emit(FavoriteUiState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoriteUiState.Loading
        )

    fun toggleFavorite(book: BookEntity) {
        viewModelScope.launch {
            bookshelfRepository.toggleFavorite(book)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                FavoriteViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}