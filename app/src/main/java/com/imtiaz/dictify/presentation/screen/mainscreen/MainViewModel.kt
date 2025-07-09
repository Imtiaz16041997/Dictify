package com.imtiaz.dictify.presentation.screen.mainscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.model.WordsInformation
import com.imtiaz.dictify.domain.repository.DictionaryRepository

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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- MainUiState ---
// This class should be in its own file (e.g., MainUiState.kt) or defined directly in ViewModel
data class MainUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val wordDefinition: WordsInformation? = null // New field for dictionary result
)
// --- End MainUiState ---

@HiltViewModel
class MainViewModel @Inject constructor(


    private val dictionaryRepo: DictionaryRepository // Inject the new dictionary repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()



    // You likely have a searchTvSeries function too, similar to searchMovies

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun lookupWordDefinition(word: String) {
        viewModelScope.launch {
            flowOf(word)
                .debounce(300) // Debounce user typing to avoid excessive API calls
                .filter { it.trim().isNotEmpty() } // Only search for non-empty words
                .distinctUntilChanged() // Only search if the word itself changed
                .flatMapLatest { query -> // Cancel previous requests if a new query arrives
                    // Call the dictionary repository
                    dictionaryRepo.getWordDefinition(
                        word = query,
                        targetLang = "en", // You might make these configurable in the future
                        sourceLang = "en"
                    )
                }
                .onStart {
                    // Emit loading state and clear previous definition when search starts
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null, wordDefinition = null)
                }
                .catch { exception ->
                    // Catch any errors from the upstream flow (network, parsing)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception,
                        wordDefinition = null // Clear definition on error
                    )
                }
                .collect { result ->
                    // Collect the DataState from the repository
                    when (result) {
                        is DataState.Loading -> { /* Handled by onStart */ }
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
                                wordDefinition = null // Clear previous definition on error
                            )
                        }
                    }
                }
        }
    }
}

