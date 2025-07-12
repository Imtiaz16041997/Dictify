package com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.domain.translator.Language
import com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel.TranslatorEvent
import com.imtiaz.dictify.domain.service.TextToSpeechService
import com.imtiaz.dictify.domain.translator.LanguageRepository
import com.imtiaz.dictify.utils.FlagMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val ttsService: TextToSpeechService
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<TranslatorEvent>()
    val eventFlow: SharedFlow<TranslatorEvent> = _eventFlow.asSharedFlow()

    // StateFlow for the list of available languages
    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    // StateFlow for the currently selected source language
    private val _sourceLanguage = MutableStateFlow(
        Language(
            "en",
            "English",
            FlagMapper.getFlagResIdForLanguageCode( code= "en") // Use the utility function for initial flags
        )
    )
    val sourceLanguage: StateFlow<Language> = _sourceLanguage.asStateFlow()

    // StateFlow for the currently selected target language
    private val _targetLanguage = MutableStateFlow(
        Language(
            "es",
            "Spanish",
            FlagMapper.getFlagResIdForLanguageCode( code= "es") // Use the utility function for initial flags
        )
    )
    val targetLanguage: StateFlow<Language> = _targetLanguage.asStateFlow()

    // --- Translation Input/Output States ---
    private val _inputText = MutableStateFlow("") // New: To hold the user's input text
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText.asStateFlow()

    private val _isLoadingTranslation = MutableStateFlow(false)
    val isLoadingTranslation: StateFlow<Boolean> = _isLoadingTranslation.asStateFlow()

    private val _translationError = MutableStateFlow<String?>(null)
    val translationError: StateFlow<String?> = _translationError.asStateFlow()


    // Add these state variables
    private val _showSourceLanguageDialog = MutableStateFlow(false)
    val showSourceLanguageDialog: StateFlow<Boolean> = _showSourceLanguageDialog.asStateFlow()

    private val _showTargetLanguageDialog = MutableStateFlow(false)
    val showTargetLanguageDialog: StateFlow<Boolean> = _showTargetLanguageDialog.asStateFlow()

    // Add functions to open/close the dialogs
    fun openSourceLanguageDialog() {
        _showSourceLanguageDialog.value = true
    }

    fun closeSourceLanguageDialog() {
        _showSourceLanguageDialog.value = false
    }

    fun openTargetLanguageDialog() {
        _showTargetLanguageDialog.value = true
    }

    fun closeTargetLanguageDialog() {
        _showTargetLanguageDialog.value = false
    }

    fun speakInputText() {
        if (_inputText.value.isNotBlank()) {
            ttsService.speak(_inputText.value, _sourceLanguage.value.language)
        } else {
            // Emit a toast event if there's no text to speak
            viewModelScope.launch {
                _eventFlow.emit(TranslatorEvent.ShowToast("No text to speak."))
            }
        }
    }

    fun speakTranslatedText() {
        if (_translatedText.value.isNotBlank()) {
            ttsService.speak(_translatedText.value, _targetLanguage.value.language)
        } else {
            // Emit a toast event if there's no translated text
            viewModelScope.launch {
                _eventFlow.emit(TranslatorEvent.ShowToast("No translated text to speak."))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsService.shutdown() // Essential for releasing TTS resources
    }


    init {
        fetchLanguages()
    }

    private fun fetchLanguages() {
        viewModelScope.launch {
            languageRepository.getLanguages().onEach { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        val fetchedLanguages = dataState.data
                        _languages.value = fetchedLanguages

                        // Set initial source/target languages based on fetched data
                        if (fetchedLanguages.isNotEmpty()) {
                            // Try to find "en" and "es" from the fetched list
                            val defaultEn = fetchedLanguages.firstOrNull { it.language == "en" }
                            val defaultEs = fetchedLanguages.firstOrNull { it.language == "es" }

                            _sourceLanguage.value = defaultEn ?: fetchedLanguages.first()
                            _targetLanguage.value = defaultEs ?: fetchedLanguages.getOrElse(1) { fetchedLanguages.first() } // Fallback to 2nd or 1st
                        }
                    }
                    is DataState.Error -> {
                        Log.e("TranslatorViewModel", "Failed to fetch languages: ${dataState.exception.localizedMessage}")
                        _translationError.value = "Failed to load languages: ${dataState.exception.localizedMessage}"
                    }
                    DataState.Loading -> {
                        // Optionally update a loading state for the language list itself if needed
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun setInputText(text: String) {
        _inputText.value = text
        // Optionally trigger translation here as user types, with a debounce
        // For now, let's keep it manual or on a specific trigger.
    }

    fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
        // Re-trigger translation if input text exists
        if (_inputText.value.isNotBlank()) {
            translateText(_inputText.value)
        }
    }

    fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
        // Re-trigger translation if input text exists
        if (_inputText.value.isNotBlank()) {
            translateText(_inputText.value)
        }
    }

    fun swapLanguages() {
        val currentSource = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = currentSource

        // Swap input and translated texts as well
        val tempInput = _inputText.value
        _inputText.value = _translatedText.value
        _translatedText.value = tempInput

        // Re-trigger translation with the newly swapped languages and input text
        if (_inputText.value.isNotBlank()) {
            translateText(_inputText.value)
        }
    }

    fun translateText(inputTextToTranslate: String) {
        _translationError.value = null // Clear previous error
        if (inputTextToTranslate.isBlank()) {
            _translatedText.value = ""
            return
        }

        _isLoadingTranslation.value = true
        viewModelScope.launch {
            languageRepository.translateText(
                text = inputTextToTranslate,
                sourceLang = _sourceLanguage.value.language,
                targetLang = _targetLanguage.value.language
            ).onEach { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _translatedText.value = dataState.data
                        _isLoadingTranslation.value = false
                    }
                    is DataState.Error -> {
                        _translatedText.value = "Error" // Or clear it
                        _translationError.value = dataState.exception.localizedMessage ?: "Translation failed"
                        _isLoadingTranslation.value = false
                    }
                    DataState.Loading -> {
                        _isLoadingTranslation.value = true
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun clearInputText() {
        _inputText.value = ""
        _translatedText.value = ""
        _translationError.value = null
        _isLoadingTranslation.value = false
    }
}