package com.imtiaz.dictify.presentation.screen.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtiaz.dictify.presentation.component.dictionary.WordDefinitionCard
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.component.common.ErrorCard
import com.imtiaz.dictify.presentation.component.dictionary.WordExpandableDefinition
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.MainViewModel
import java.util.Locale


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isCurrentWordFavorite by viewModel.isCurrentWordFavorite.collectAsState()
    val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        lateinit var ttsInstance: TextToSpeech

        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance.language = Locale.US
            } else {
                Log.e("HomeScreen", "TextToSpeech initialization failed: $status")
            }
        }

        ttsInstance = TextToSpeech(context, listener)
        textToSpeechState.value = ttsInstance

        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
            Log.d("HomeScreen", "TextToSpeech shutdown.")
        }
    }

    val textToSpeech = textToSpeechState.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        val firstWordDefinition = uiState.wordDefinition.firstOrNull()

        if (uiState.isLoading && uiState.wordDefinition.isEmpty() && uiState.error == null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (uiState.error != null) {
            item {

                ErrorCard(
                    errorMessage = "${uiState.error?.message ?: "Oops! Word not found. Please double-check the spelling."}",
                    onRetryClicked = {
                        val lastSearchedWord = viewModel.searchQuery.value
                        viewModel.triggerWordLookup(lastSearchedWord)
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (firstWordDefinition != null) {
            item {
                WordDefinitionCard(
                    word = firstWordDefinition.word.toString(),
                    definition = firstWordDefinition.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition
                        ?: "Definition not available.",
                    onRefreshClicked = {
                        firstWordDefinition.word.let { word ->
                            viewModel.triggerWordLookup(word.toString())
                        }
                    },
                    onCopyClicked = { textToCopy ->
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Dictionary Definition", textToCopy)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, clipData.toString(), Toast.LENGTH_SHORT).show()
                    },
                    onPronounceClicked = { wordToPronounce ->
                        val audioUrl =
                            firstWordDefinition.phonetics.firstOrNull { it.audio?.isNotBlank() == true }?.audio
                        if (audioUrl != null) {
                            textToSpeech?.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")
                        } else {
                            textToSpeech?.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")
                        }
                    },
                    isFavorite = isCurrentWordFavorite,
                    onFavoriteClicked = {
                        viewModel.toggleFavoriteWord(firstWordDefinition) // NEW: Call ViewModel function
                    }

                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Define your desired dark colors here
            val darkHeaderBg = Color(0xFF2C2C2C) // Example: Darker grey
            val darkContentBg = Color(0xFF1E1E1E) // Example: Even darker grey/black
            val lightTextColor = Color.White
            val veryLightTextColor = Color.LightGray

            firstWordDefinition.meanings?.forEachIndexed { meaningIndex, meaning ->
                // 1. Definitions Section
                if (!meaning.definitions.isNullOrEmpty()) {
                    item(key = "meaning-${meaningIndex}-definitions-${meaning.partOfSpeech}") {
                        val formattedTitle =
                            meaning.partOfSpeech?.replaceFirstChar { it.uppercase() }
                                ?.let { "$it Definitions" } ?: "Definitions"
                        val formattedContent =
                            meaning.definitions.joinToString("\n\n") { definitionItem ->
                                val defText = "♦ ${definitionItem.definition}"
                                val exampleText =
                                    definitionItem.example?.let { "\nExample: $it" } ?: ""
                                "$defText$exampleText"
                            }
                        WordExpandableDefinition(
                            title = formattedTitle,
                            content = formattedContent,
                            initialExpanded = meaningIndex == 0,
                            modifier = Modifier.fillMaxWidth(),
                            titleColor = lightTextColor,
                            contentColor = veryLightTextColor,
                            headerBackgroundColor = darkHeaderBg, // Use custom color
                            contentBackgroundColor = darkContentBg // Use custom color
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // 2. Synonyms Section
                if (!meaning.synonyms.isNullOrEmpty()) {
                    item(key = "meaning-${meaningIndex}-synonyms-${meaning.partOfSpeech}") {
                        val formattedTitle =
                            meaning.partOfSpeech?.replaceFirstChar { it.uppercase() }
                                ?.let { "$it Synonyms" } ?: "Synonyms"
                        val formattedContent = meaning.synonyms.joinToString(", ")
                        WordExpandableDefinition(
                            title = formattedTitle,
                            content = formattedContent,
                            initialExpanded = false,
                            modifier = Modifier.fillMaxWidth(),
                            titleColor = lightTextColor,
                            contentColor = veryLightTextColor,
                            headerBackgroundColor = darkHeaderBg, // Use custom color
                            contentBackgroundColor = darkContentBg // Use custom color
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // 3. Antonyms Section
                if (!meaning.antonyms.isNullOrEmpty()) {
                    item(key = "meaning-${meaningIndex}-antonyms-${meaning.partOfSpeech}") {
                        val formattedTitle =
                            meaning.partOfSpeech?.replaceFirstChar { it.uppercase() }
                                ?.let { "$it Antonyms" } ?: "Antonyms"
                        val formattedContent = meaning.antonyms.joinToString(", ")
                        WordExpandableDefinition(
                            title = formattedTitle,
                            content = formattedContent,
                            initialExpanded = false,
                            modifier = Modifier.fillMaxWidth(),
                            titleColor = lightTextColor,
                            contentColor = veryLightTextColor,
                            headerBackgroundColor = darkHeaderBg, // Use custom color
                            contentBackgroundColor = darkContentBg // Use custom color
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

