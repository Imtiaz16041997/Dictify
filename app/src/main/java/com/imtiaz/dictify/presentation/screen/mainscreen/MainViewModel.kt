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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val wordDefinition: List<WordResponse> = emptyList(),
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun lookupWordDefinition(word: String) {
        viewModelScope.launch {
            flowOf(word)
                // Debounce/distinct can be kept but are less critical for explicit triggers.
                // They might still prevent rapid multiple taps of the search button.
                .debounce(100) // Small debounce to handle very rapid double-taps
                .distinctUntilChanged() // Ensures API isn't called for the same word repeatedly

                .filter { it.trim().isNotEmpty() } // <--- Only proceed if the word is not empty
                .flatMapLatest { query ->
                    dictionaryRepo.getWordDefinition(
                        word = query
                    )
                }
                .onStart {
                    // Emit loading state and clear previous definition
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null, wordDefinition = emptyList())
                }
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception,
                        wordDefinition = emptyList()
                    )
                }
                .collect { result ->
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
                                wordDefinition = emptyList()
                            )
                        }
                    }
                }
        }

        // Handle the case where the input word is empty.
        // This is important for the `onCloseClicked` from MySearchBar
        if (word.isBlank()) {
            _uiState.value = _uiState.value.copy(
                wordDefinition = emptyList(), // Clear the definition
                isLoading = false,
                error = null
            )
        }
    }
}