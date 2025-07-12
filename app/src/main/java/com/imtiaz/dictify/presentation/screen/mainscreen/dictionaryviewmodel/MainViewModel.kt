package com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository
import com.imtiaz.dictify.domain.model.dictionary.FavoriteWord
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

/*@HiltViewModel
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
                        is DataState.Loading -> { *//* Handled by initial _uiState update in flatMapLatest *//* }
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


    *//**
     * Updates the text in the search bar (UI) and triggers a debounced search API call.
     * This should be called by the UI's onTextChange callback for the search TextField.
     *//*
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query // Update UI state
        viewModelScope.launch {
            _typingSearchApiTrigger.emit(query) // Trigger debounced API call
        }
    }

    *//**
     * This function is specifically for explicit search triggers:
     * - When the user presses the search icon in the keyboard.
     * - When the user presses the leading search icon in the search bar.
     * - When voice input provides a result.
     * It updates the `_searchQuery` for UI display and triggers an immediate search API call
     * via the _explicitSearchApiTrigger SharedFlow.
     *//*
    fun triggerWordLookup(word: String) {
        // Update the search bar text for UI consistency
        _searchQuery.value = word
        // Emit to the explicit API trigger flow to initiate an immediate search
        viewModelScope.launch {
            _explicitSearchApiTrigger.emit(word)
        }
    }
}*/

/*
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository // This already exists
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    private val _typingSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val _explicitSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)

    // NEW: StateFlow to observe if the currently displayed word is a favorite
    private val _isCurrentWordFavorite = MutableStateFlow(false)
    val isCurrentWordFavorite: StateFlow<Boolean> = _isCurrentWordFavorite.asStateFlow()

    *//*init {
        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            merge(
                _typingSearchApiTrigger
                    .debounce(300L)
                    .distinctUntilChanged()
                    .filter { it.trim().isNotEmpty() },

                _explicitSearchApiTrigger
                    .filter { it.trim().isNotEmpty() }
            )
                .flatMapLatest { query ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        wordDefinition = emptyList()
                    )
                    // Observe favorite status for the *new* word being searched
                    dictionaryRepo.isWordFavorite(query).collect { isFavorite ->
                        _isCurrentWordFavorite.value = isFavorite
                    }
                    dictionaryRepo.getWordDefinition(word = query)
                        .catch { exception ->
                            emit(DataState.Error(exception as Exception))
                        }
                }
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> { *//**//* Handled by initial _uiState update in flatMapLatest *//**//* }
                        is DataState.Success -> {
                            _uiState.value = _uiState.value.copy(
                                wordDefinition = result.data,
                                isLoading = false,
                                error = null
                            )
                            // After a successful definition fetch, update favorite status
                            _uiState.value.wordDefinition.firstOrNull()?.word?.let { word ->
                                dictionaryRepo.isWordFavorite(word).collect { isFavorite ->
                                    _isCurrentWordFavorite.value = isFavorite
                                }
                            }
                        }
                        is DataState.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.exception,
                                wordDefinition = emptyList()
                            )
                            _isCurrentWordFavorite.value = false // Clear favorite status on error
                        }
                    }
                }
        }

        viewModelScope.launch {
            _searchQuery
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            wordDefinition = emptyList(),
                            isLoading = false,
                            error = null
                        )
                        _isCurrentWordFavorite.value = false // Clear favorite status when search is blank
                    }
                }
        }
    }*//*

    init {
        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            // This launch block will collect the search queries and trigger API calls
            merge(
                _typingSearchApiTrigger
                    .debounce(300L)
                    .distinctUntilChanged()
                    .filter { it.trim().isNotEmpty() },

                _explicitSearchApiTrigger
                    .filter { it.trim().isNotEmpty() }
            )
                .flatMapLatest { query ->
                    // Set loading state for UI
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        wordDefinition = emptyList()
                    )

                    // Trigger the API call (this returns a Flow<DataState<List<WordResponse>>>)
                    dictionaryRepo.getWordDefinition(word = query)
                        .catch { exception ->
                            emit(DataState.Error(exception as Exception)) // Catch errors and emit to the main flow
                        }
                }
                .collect { result ->
                    // Collect the results from the API call
                    when (result) {
                        is DataState.Loading -> { *//* This will usually not be hit here as we set isLoading before flatMapLatest *//*
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


        fun updateSearchQuery(query: String) {
            _searchQuery.value = query
            viewModelScope.launch {
                _typingSearchApiTrigger.emit(query)
            }
        }

        fun triggerWordLookup(word: String) {
            _searchQuery.value = word
            viewModelScope.launch {
                _explicitSearchApiTrigger.emit(word)
            }
        }

        // NEW: Functions to handle favorite actions
        *//*fun toggleFavoriteWord(wordResponse: WordResponse) {
            viewModelScope.launch {
                val word = wordResponse.word ?: return@launch
                val isCurrentlyFavorite = _isCurrentWordFavorite.value

                if (isCurrentlyFavorite) {
                    // If it's already a favorite, unfavorite it
                    dictionaryRepo.deleteFavoriteWord(word)
                } else {
                    // If not a favorite, add it
                    val definition =
                        wordResponse.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
                    val partOfSpeech = wordResponse.meanings.firstOrNull()?.partOfSpeech
                    val audioUrl =
                        wordResponse.phonetics.firstOrNull { it.audio?.isNotBlank() == true }?.audio

                    val favoriteWord = FavoriteWord(word, definition, partOfSpeech, audioUrl)
                    dictionaryRepo.saveFavoriteWord(favoriteWord)
                }
                // The _isCurrentWordFavorite flow will automatically update via the isWordFavorite() call in flatMapLatest/collect
                // No need to manually set _isCurrentWordFavorite.value here.
            }
        }*//*

        viewModelScope.launch {
            // Listen to changes in the current word being displayed
            _uiState.map { it.wordDefinition.firstOrNull()?.word }
                .distinctUntilChanged() // Only react when the word itself changes
                .collect { currentWord ->
                    if (currentWord != null && currentWord.isNotBlank()) {
                        // If there's a valid word, observe its favorite status
                        dictionaryRepo.isWordFavorite(currentWord)
                            .collect { isFavorite ->
                                _isCurrentWordFavorite.value = isFavorite
                            }
                    } else {
                        // No word displayed, or word is blank, so it's not a favorite
                        _isCurrentWordFavorite.value = false
                    }
                }
        }


        // Separate collector for UI clearing logic when the search bar becomes blank.
        viewModelScope.launch {
            _searchQuery
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            wordDefinition = emptyList(),
                            isLoading = false,
                            error = null
                        )
                        _isCurrentWordFavorite.value = false // Also reset favorite status
                    }
                }
        }
    }
}*/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    private val _typingSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val _explicitSearchApiTrigger = MutableSharedFlow<String>(extraBufferCapacity = 1)

    // StateFlow to observe if the currently *displayed and loaded* word is a favorite
    private val _isCurrentWordFavorite = MutableStateFlow(false)
    val isCurrentWordFavorite: StateFlow<Boolean> = _isCurrentWordFavorite.asStateFlow()

    init {
        @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            merge(
                _typingSearchApiTrigger
                    .debounce(300L)
                    .distinctUntilChanged()
                    .filter { it.trim().isNotEmpty() },

                _explicitSearchApiTrigger
                    .filter { it.trim().isNotEmpty() }
            )
                .flatMapLatest { query ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        wordDefinition = emptyList()
                    )
                    // Only trigger the API call for definition here
                    dictionaryRepo.getWordDefinition(word = query)
                        .catch { exception ->
                            emit(DataState.Error(exception as Exception))
                        }
                }
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> { /* Not used here, state set before flatMapLatest */ }
                        is DataState.Success -> {
                            _uiState.value = _uiState.value.copy(
                                wordDefinition = result.data,
                                isLoading = false,
                                error = null
                            )
                            // NEW: After successful API call and UI state update,
                            // explicitly check favorite status for the fetched word.
                            result.data.firstOrNull()?.word?.let { fetchedWord ->
                                updateFavoriteStatus(fetchedWord)
                            } ?: run {
                                // If no word in response, it's not favorited
                                _isCurrentWordFavorite.value = false
                            }
                        }
                        is DataState.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.exception,
                                wordDefinition = emptyList()
                            )
                            // On error, the displayed word is effectively gone, so not favorited
                            _isCurrentWordFavorite.value = false
                        }
                    }
                }
        }

        // This launch block keeps the _searchQuery and UI clear logic separate
        viewModelScope.launch {
            _searchQuery
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            wordDefinition = emptyList(),
                            isLoading = false,
                            error = null
                        )
                        _isCurrentWordFavorite.value = false // Clear favorite status when search is blank
                    }
                }
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _typingSearchApiTrigger.emit(query)
        }
    }

    fun triggerWordLookup(word: String) {
        _searchQuery.value = word
        viewModelScope.launch {
            _explicitSearchApiTrigger.emit(word)
        }
    }

    // NEW: Helper function to update the _isCurrentWordFavorite status
    private fun updateFavoriteStatus(word: String) {
        viewModelScope.launch {
            dictionaryRepo.isWordFavorite(word).collect { isFavorite ->
                _isCurrentWordFavorite.value = isFavorite
            }
        }
    }

    // This function remains the same, as it correctly uses the displayed word data
    fun toggleFavoriteWord(wordResponse: WordResponse) {
        viewModelScope.launch {
            val word = wordResponse.word ?: return@launch
            val isCurrentlyFavorite = _isCurrentWordFavorite.value // Use the current UI state of favorite

            if (isCurrentlyFavorite) {
                dictionaryRepo.deleteFavoriteWord(word)
                // Manually update the state after action for immediate UI feedback
                _isCurrentWordFavorite.value = false
            } else {
                val definition = wordResponse.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
                val partOfSpeech = wordResponse.meanings.firstOrNull()?.partOfSpeech
                val audioUrl = wordResponse.phonetics.firstOrNull { it.audio?.isNotBlank() == true }?.audio

                val favoriteWord = FavoriteWord(word, definition, partOfSpeech, audioUrl)
                dictionaryRepo.saveFavoriteWord(favoriteWord)
                // Manually update the state after action for immediate UI feedback
                _isCurrentWordFavorite.value = true
            }
            // Optional: You could also call updateFavoriteStatus(word) here to re-collect from DB
            // but for immediate UI feedback after a toggle, directly setting the value is often preferred.
        }
    }
}


data class MainUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val wordDefinition: List<WordResponse> = emptyList(),
)