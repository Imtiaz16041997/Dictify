package com.imtiaz.dictify.data.repository.remote.common

sealed class TranslatorEvent {
    data class ShowToast(val message: String) : TranslatorEvent()
    }