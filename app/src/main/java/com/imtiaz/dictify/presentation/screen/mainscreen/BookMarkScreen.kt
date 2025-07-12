package com.imtiaz.dictify.presentation.screen.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.ClipboardManager
import android.content.ClipData
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.Locale
import android.util.Log
import com.imtiaz.dictify.presentation.component.dictionary.FavoriteWordCard
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.BookmarkViewModel

@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: BookmarkViewModel = hiltViewModel() // Use BookmarkViewModel
) {
    val favoriteWords by viewModel.favoriteWords.collectAsState()
    val context = LocalContext.current

    // TTS for pronunciation in Bookmarks
    val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        lateinit var ttsInstance: TextToSpeech
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance.language = Locale.US // Default to US English
                Log.d("BookmarksScreen", "TextToSpeech initialized successfully.")
            } else {
                Log.e("BookmarksScreen", "TextToSpeech initialization failed: $status")
            }
        }
        ttsInstance = TextToSpeech(context, listener)
        textToSpeechState.value = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
            Log.d("BookmarksScreen", "TextToSpeech shutdown.")
        }
    }
    val textToSpeech = textToSpeechState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface) // Use background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Text(
            text = "Bookmarks",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )*/

        if (favoriteWords.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite words yet.",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteWords, key = { it.word }) { favoriteWord ->
                    FavoriteWordCard(
                        favoriteWord = favoriteWord,
                        onDeleteClick = { viewModel.removeFavoriteWord(favoriteWord.word) },
                        onPronounceClick = { wordToPronounce ->
                            if (textToSpeech != null && wordToPronounce.isNotBlank()) {
                                // You might want to try to set the locale for pronunciation
                                val result = textToSpeech.setLanguage(Locale.forLanguageTag("en")) // Assuming English for dictionary words
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(context, "Language not supported for TTS.", Toast.LENGTH_SHORT).show()
                                } else {
                                    textToSpeech.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")
                                }
                            } else {
                                Toast.makeText(context, "TTS not ready or no text.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onCopyClick = { textToCopy ->
                            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("Favorite Word Definition", textToCopy)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}