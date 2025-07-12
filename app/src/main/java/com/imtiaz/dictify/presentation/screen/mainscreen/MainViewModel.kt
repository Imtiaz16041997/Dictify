package com.imtiaz.dictify.presentation.screen.mainscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge




@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    // Centralized search query state (text in the search bar), purely for UI display
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    // NEW: A SharedFlow specifically for API calls triggered by typing (debounced)
    private val _typingSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)

    // A SharedFlow for explicit search triggers (button click, voice input, refresh)
    private val _explicitSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            // Merge the two distinct API trigger flows (typing and explicit)
            merge(
                // 1. "Search-as-you-type" API trigger flow:
                _typingSearchApiTrigger // NEW: Now observing this SharedFlow for typing API calls
                    .debounce(300L) // Debounce typing input
                    .distinctUntilChanged() // Ensure distinct words from typing
                    .filter { it.trim().isNotEmpty() }, // Only search non-empty words

                // 2. Explicit API trigger flow:
                _explicitSearchApiTrigger // Observing this SharedFlow for explicit API calls
                    .filter { it.trim().isNotEmpty() } // Still filter empty strings for API call
            )
                .flatMapLatest { query -> // flatMapLatest ensures only the latest request is active
                    // Emit loading state before the actual API call starts
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        wordDefinition = emptyList()
                    )
                    // Call the repository to get the definition
                    dictionaryRepo.getWordDefinition(word = query)
                        .catch { exception ->
                            emit(DataState.Error(exception as Exception)) // Emit error into the stream
                        }
                }
                .collect { result ->
                    // Collect the final result (Success or Error)
                    when (result) {
                        is DataState.Loading -> { /* Handled by initial _uiState update in flatMapLatest */ }
                        is DataState.Success -> {
                            _uiState.value = _uiState.value.copy(
                                wordDefinition = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is DataState.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.exception,
                                wordDefinition = emptyList()
                            )
                        }
                    }
                }
        }

        // Separate collector for UI clearing logic when the search bar becomes blank.
        // This is still linked to _searchQuery for consistency and immediate UI updates.
        viewModelScope.launch {
            _searchQuery
                // No distinctUntilChanged needed as StateFlow is inherently distinct
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            wordDefinition = emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    /**
     * Updates the text in the search bar (UI) and triggers a debounced search API call.
     * This should be called by the UI's onTextChange callback for the search TextField.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query // Update UI state
        viewModelScope.launch {
            _typingSearchApiTrigger.emit(query) // Trigger debounced API call
        }
    }

    /**
     * This function is specifically for explicit search triggers:
     * - When the user presses the search icon in the keyboard.
     * - When the user presses the leading search icon in the search bar.
     * - When voice input provides a result.
     * It updates the `_searchQuery` for UI display and triggers an immediate search API call
     * via the _explicitSearchApiTrigger SharedFlow.
     */
    fun triggerWordLookup(word: String) {
        // Update the search bar text for UI consistency
        _searchQuery.value = word
        // Emit to the explicit API trigger flow to initiate an immediate search
        viewModelScope.launch {
            _explicitSearchApiTrigger.emit(word)
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val wordDefinition: List<WordResponse> = emptyList(),
)