package com.imtiaz.dictify.data.repository.remote.common

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.imtiaz.dictify.domain.repository.common.TextToSpeechService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class TextToSpeechServiceImpl @Inject constructor(@ApplicationContext private val context: Context) :
    TextToSpeechService {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                Log.d("TextToSpeechService", "TextToSpeech initialized successfully.")
            } else {
                Log.e("TextToSpeechService", "TextToSpeech initialization failed: $status")
            }
        }
    }

    override fun speak(text: String, languageCode: String) {
        if (!isInitialized) { // Checks if TTS is NOT initialized
            Log.e("TextToSpeechService", "TTS not initialized yet. Cannot speak: $text")

            return
        }

        // If it reaches here, it means isInitialized is true, so proceed with speaking
        val locale = Locale(languageCode.lowercase())
        val result = tts?.setLanguage(locale)

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TextToSpeechService", "TTS language not supported: $languageCode")
            // ...
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    /*override fun speak(text: String, languageCode: String) {
        if (isInitialized) {
            val locale = Locale(languageCode.lowercase())
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeechService", "TTS language not supported: $languageCode")
                // You might emit an event back to ViewModel here
            } else {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        } else {
            Log.e("TextToSpeechService", "TTS not initialized.")
        }
    }*/

    override fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        isInitialized = false
        tts = null
        Log.d("TextToSpeechService", "TextToSpeech shutdown.")
    }
}