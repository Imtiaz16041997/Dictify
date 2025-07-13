package com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel

sealed class TranslatorEvent {
    data class ShowToast(val message: String) : TranslatorEvent()
    }