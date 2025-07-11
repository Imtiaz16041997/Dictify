package com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.data.model.translation.Language
import com.imtiaz.dictify.domain.repository.translator.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages

    private val _sourceLanguage = MutableStateFlow(Language(language = "EN", name = "English"))
    val sourceLanguage: StateFlow<Language> = _sourceLanguage

    private val _targetLanguage = MutableStateFlow(Language(language = "ES", name = "Spanish"))
    val targetLanguage: StateFlow<Language> = _targetLanguage

    // Add other state for input/output text, loading, etc. here

    init {
        fetchLanguages()
    }

    private fun fetchLanguages() {
        viewModelScope.launch {
            languageRepository.getLanguages().collect { languagesList ->
                _languages.value = languagesList
            }
        }
    }

    fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
    }

    fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
    }

    fun swapLanguages() {
        _sourceLanguage.value = targetLanguage.value
        _targetLanguage.value = sourceLanguage.value
    }
}