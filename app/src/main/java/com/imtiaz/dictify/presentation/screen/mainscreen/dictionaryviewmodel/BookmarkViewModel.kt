package com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository
import com.imtiaz.dictify.domain.model.dictionary.FavoriteWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepository
) : ViewModel() {

    private val _favoriteWords = MutableStateFlow<List<FavoriteWord>>(emptyList())
    val favoriteWords: StateFlow<List<FavoriteWord>> = _favoriteWords.asStateFlow()

    init {
        fetchFavoriteWords()
    }

    private fun fetchFavoriteWords() {
        viewModelScope.launch {
            dictionaryRepo.getAllFavoriteWords().onEach { words ->
                _favoriteWords.value = words
            }.launchIn(viewModelScope)
        }
    }

    fun removeFavoriteWord(word: String) {
        viewModelScope.launch {
            dictionaryRepo.deleteFavoriteWord(word)
        }
    }


}