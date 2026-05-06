package com.example.bookshelf.ui.screens.search

    import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.repository.BookshelfRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "SearchViewModel"

data class SearchRawUiState(
    val query: String = "",
)

sealed interface SearchUiState {
    data class Success(val books: List<BookEntity>, val isOffline: Boolean) : SearchUiState
    data class Error(val message: String): SearchUiState
    object Loading: SearchUiState
    object Empty: SearchUiState
    object NoResults: SearchUiState
}

sealed interface SearchUiEvent {
    data class ShowSnackbar(val message: String): SearchUiEvent
}

class SearchViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    private val _searchRawUiState = MutableStateFlow(SearchRawUiState())
    private val networkError = MutableStateFlow<String?>(null)
    private val isRequestFinished = MutableStateFlow(false)

    private val _events = MutableSharedFlow<SearchUiEvent>()
    val events = _events.asSharedFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = _searchRawUiState
        .map { it.query }
        .debounce(1000)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.length < 3) {
                flowOf<SearchUiState>(SearchUiState.Empty)
            } else {
                networkError.value = null
                isRequestFinished.value = false
                triggerApi(query)

                combine(
                    bookshelfRepository.observeSearchResults(query),
                    networkError,
                    isRequestFinished
                ) { list, error, finished ->
                    when {
                        list.isEmpty() && error != null && finished -> {
                            SearchUiState.Error("Network error")
                        }

                        list.isEmpty() && finished -> {
                            SearchUiState.NoResults
                        }

                        list.isEmpty() -> {
                            SearchUiState.Loading
                        }

                        else -> {
                            SearchUiState.Success(
                                books = list,
                                isOffline = error != null
                            )
                        }
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Empty
        )

    private var searchJob: Job? = null
    private fun triggerApi(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                bookshelfRepository.refreshSearch(query)
                networkError.value = null
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                networkError.value = "Network error"
                _events.emit(SearchUiEvent.ShowSnackbar(message = "Offline Mode"))
            } finally {
                isRequestFinished.value = true
            }
        }
    }

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