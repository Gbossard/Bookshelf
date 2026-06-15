package com.example.bookshelf.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.repository.BookshelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FavoriteViewModel"
sealed interface FavoriteUiState {
    data class Success(val favorites: List<BookEntity>) : FavoriteUiState
    object Error: FavoriteUiState
    object Loading: FavoriteUiState
}

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {
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
}