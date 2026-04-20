package com.example.bookshelf.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

data class SearchRawUiState(
    val query: String = "",
)

sealed interface SearchUiState {
    data class Success(val books: List<BookEntity>) : SearchUiState
    object Error: SearchUiState
    object Loading: SearchUiState
    object Empty: SearchUiState
    object NoResults: SearchUiState
}

class SearchViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    private val _searchRawUiState = MutableStateFlow(SearchRawUiState())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = _searchRawUiState
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { state ->
                if (state.query.length < 3) {
                    flowOf<SearchUiState>(SearchUiState.Empty)
                } else {
                    flow {
                        emit(SearchUiState.Loading)
                        try {
                            bookshelfRepository.searchBooks(state.query)
                        } catch (error: Exception) {
                            Log.e(TAG, "Unexpected error searchBooks", error)
                        }

                        emitAll(bookshelfRepository.getSearchResults().map { list ->
                            val filteredList = list.filter { book ->
                                book.title.contains(state.query, ignoreCase = true) ||
                                book.authors?.contains(state.query, ignoreCase = true) == true
                            }
                            if (filteredList.isEmpty()) SearchUiState.NoResults
                            else SearchUiState.Success(filteredList)
                        })
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SearchUiState.Empty
            )

    val searchQuery: StateFlow<String> = _searchRawUiState
        .map { it.query }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ""
        )

    fun onQueryChange(newQuery: String) {
        _searchRawUiState.update {
            it.copy(query = newQuery)
        }
    }

    fun onClearQuery() {
        _searchRawUiState.update {
            it.copy(query = "")
        }
    }

    fun toggleFavorite(book: BookEntity) {
        viewModelScope.launch {
            bookshelfRepository.toggleFavorite(book)
        }
    }
}