package com.imtiaz.dictify.presentation.screen.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.component.random.DailyWordCard
import com.imtiaz.dictify.presentation.screen.mainscreen.history.HistoryViewModel
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel() // Inject HistoryViewModel
) {
    val dailyWords by viewModel.dailyWords.collectAsState()
    val context = LocalContext.current

    // TTS for pronunciation in HistoryScreen
    val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        lateinit var ttsInstance: TextToSpeech
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance.language = Locale.US // Default to US English
                Log.d("HistoryScreen", "TextToSpeech initialized successfully.")
            } else {
                Log.e("HistoryScreen", "TextToSpeech initialization failed: $status")
            }
        }
        ttsInstance = TextToSpeech(context, listener)
        textToSpeechState.value = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
            Log.d("HistoryScreen", "TextToSpeech shutdown.")
        }
    }
    val textToSpeech = textToSpeechState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use your app's background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (dailyWords.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No word insights yet.",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dailyWords, key = { it.id }) { dailyWord -> // Use unique ID for key
                    DailyWordCard( // Use a new or adapted card for daily words
                        dailyWord = dailyWord,
                        onPronounceClick = { wordToPronounce ->
                            if (textToSpeech != null && wordToPronounce.isNotBlank()) {
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
                            val clipData = ClipData.newPlainText("Daily Word Insight", textToCopy)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                        },
                        onCardClick = {
                           // onNavigateToDetail(dailyWord.word) // Navigate to detail when card is clicked
                        }
                    )
                }
            }
        }
    }
}


