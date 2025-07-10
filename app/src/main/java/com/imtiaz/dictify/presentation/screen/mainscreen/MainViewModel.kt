package com.imtiaz.dictify.presentation.screen.mainscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.data.model.dictzilla.WordsInformation
import com.imtiaz.dictify.domain.repository.DictionaryRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    // Centralized search query state (text in the search bar)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    init {
        // This block listens to changes in the _searchQuery (user typing, voice input)
        // and automatically triggers the word lookup with debounce and distinct checks.
        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            _searchQuery
                .debounce(300L) // Wait for 300ms of no new input before triggering a search
                .distinctUntilChanged() // Only trigger if the query is actually different
                // Removed .filter { it.trim().isNotEmpty() } here, as performWordLookup handles blank
                .collectLatest { query ->
                    // This will call the actual lookup logic whenever _searchQuery changes
                    // after debouncing and distinct filtering.
                    performWordLookup(query)
                }
        }
    }

    /**
     * Updates the text in the search bar. This should be called by the UI's
     * onTextChange callback for the search TextField.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * This function contains the core logic for looking up a word definition.
     * It's called internally by the `_searchQuery` flow (for search-as-you-type)
     * and externally by `triggerWordLookup` (for explicit searches).
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun performWordLookup(word: String) {
        // Handle the case where the input word is empty.
        // This is crucial for the `onCloseClicked` from MySearchBar
        if (word.isBlank()) {
            _uiState.value = _uiState.value.copy(
                wordDefinition = emptyList(), // Clear the definition
                isLoading = false,
                error = null
            )
            return // Exit early if word is blank, no API call needed
        }

        viewModelScope.launch {
            dictionaryRepo.getWordDefinition(word)
                .onStart {
                    // Emit loading state and clear previous definition when a new search starts
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        wordDefinition = emptyList()
                    )
                }
                .catch { exception ->
                    // Handle any exceptions during the API call
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception,
                        wordDefinition = emptyList()
                    )
                }
                .collect { result ->
                    // Collect the final result (Success or Error from DataState)
                    when (result) {
                        is DataState.Loading -> { /* Handled by onStart or by the initial _uiState update */
                        }

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
    }

    /**
     * This function is specifically for explicit search triggers:
     * - When the user presses the search icon in the keyboard.
     * - When the user presses the leading search icon in the search bar.
     * - When voice input provides a result.
     * It updates the `_searchQuery`, which then automatically triggers the `performWordLookup`
     * via the `init` block's flow collection, ensuring all debouncing/distinct logic is applied.
     */
    fun triggerWordLookup(word: String) {
        // Update the search bar text first
        _searchQuery.value = word
        // Immediately show loading state for explicit triggers to provide instant feedback,
        // even if the debounce causes a slight delay before the API call starts.
        if (word.isNotBlank()) {
            _uiState.value =
                _uiState.value.copy(isLoading = true, error = null, wordDefinition = emptyList())
        } else {
            // If triggered with a blank word (e.g., search on empty string), clear UI
            _uiState.value =
                _uiState.value.copy(isLoading = false, error = null, wordDefinition = emptyList())
        }
    }
}


data class MainUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val wordDefinition: List<WordResponse> = emptyList(),
)