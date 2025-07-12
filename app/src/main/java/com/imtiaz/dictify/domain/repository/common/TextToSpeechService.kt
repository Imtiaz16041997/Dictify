package com.imtiaz.dictify.domain.repository.common

interface TextToSpeechService {
    fun speak(text: String, languageCode: String)
    fun shutdown()

}