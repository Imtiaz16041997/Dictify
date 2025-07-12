package com.imtiaz.dictify.domain.service

interface TextToSpeechService {
    fun speak(text: String, languageCode: String)
    fun shutdown()

}