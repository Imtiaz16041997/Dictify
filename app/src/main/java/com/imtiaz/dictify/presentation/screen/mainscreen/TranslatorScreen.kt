package com.imtiaz.dictify.presentation.screen.mainscreen

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.component.translator.LanguageSelector
import com.imtiaz.dictify.presentation.component.translator.TranslationInputCard
import com.imtiaz.dictify.presentation.component.translator.TranslationOutputCard
import com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel.TranslatorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel.TranslatorEvent
import com.imtiaz.dictify.presentation.component.translator.LanguageSelectionDialog
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TranslatorScreen(
    navController: NavController,
    viewModel: TranslatorViewModel = hiltViewModel()
) {
    // Observe states from the ViewModel
    val languages by viewModel.languages.collectAsState()
    val sourceLang by viewModel.sourceLanguage.collectAsState()
    val targetLang by viewModel.targetLanguage.collectAsState()
    val inputText by viewModel.inputText.collectAsState() // Observe input text from VM
    val translatedText by viewModel.translatedText.collectAsState() // Observe translated text from VM
    val isLoading by viewModel.isLoadingTranslation.collectAsState() // Observe loading state from VM
    val translationError by viewModel.translationError.collectAsState() // Observe error from VM

    val context = LocalContext.current

    // TextToSpeech for input and output pronunciation
   /* val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }

    /*DisposableEffect(context) {
        lateinit var ttsInstance: TextToSpeech
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance.language = Locale.US // Default for English input/output
                Log.d("TranslatorScreen", "TextToSpeech initialized successfully.")
            } else {
                Log.e("TranslatorScreen", "TextToSpeech initialization failed: $status")
            }
        }
        ttsInstance = TextToSpeech(context, listener)
        textToSpeechState.value = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
            Log.d("TranslatorScreen", "TextToSpeech shutdown.")
        }
    }*/

    viewModel.speakInputText()
    viewModel.speakTranslatedText()
    val textToSpeech = textToSpeechState.value

    */

    // Show error as a Toast
//    LaunchedEffect(translationError) {
//        translationError?.let {
//            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
//            // You might want to clear the error after showing it, e.g., viewModel.clearTranslationError()
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TranslatorEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                // Handle other events here if you add them (e.g., navigation)
            }
        }
    }

    // Speech Recognition Launcher for input mic
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenTexts = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenTexts?.get(0) ?: ""
            if (spokenText.isNotBlank()) {
                viewModel.setInputText(spokenText) // Update input text via ViewModel
                viewModel.translateText(spokenText) // Trigger translation
            }
        } else {
            Toast.makeText(context, "Voice input cancelled/failed.", Toast.LENGTH_SHORT).show()
        }
    }


    // Observe dialog visibility states
    val showSourceLanguageDialog by viewModel.showSourceLanguageDialog.collectAsState()
    val showTargetLanguageDialog by viewModel.showTargetLanguageDialog.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Language Selector
        item {
            LanguageSelector(
                sourceLanguage = sourceLang.name,
                sourceFlag = painterResource(id = sourceLang.flagResId),
                targetLanguage = targetLang.name,
                targetFlag = painterResource(id = targetLang.flagResId),
                onSourceClick = {
                    // Open source language selection dialog
                    viewModel.openSourceLanguageDialog()
                },
                onTargetClick = {
                    // Open target language selection dialog
                    viewModel.openTargetLanguageDialog()
                },
                onSwapClick = { viewModel.swapLanguages() }
            )
        }

        // 2. Translation Input Card
        item {
            TranslationInputCard(
                languageLabel = sourceLang.name,
                inputText = inputText,
                onInputTextChange = {
                    viewModel.setInputText(it)
                    // You might debounce this or add a translate button for user to trigger
                    // For now, let's keep it separate via translateText call
                },
                onClearInput = { viewModel.clearInputText() },
                onSpeakInput = {
                    viewModel.speakInputText()
                    /*if (textToSpeech != null && inputText.isNotBlank()) {
                        // Set TTS language for source, if supported
                        val locale = Locale(sourceLang.language.lowercase())
                        val result = textToSpeech.setLanguage(locale)
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TranslatorScreen", "TTS language not supported: ${sourceLang.language}")
                            Toast.makeText(context, "Language for speaking not supported: ${sourceLang.name}", Toast.LENGTH_SHORT).show()
                        } else {
                            textToSpeech.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, "")
                            Toast.makeText(context, "Speaking input...", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "No text to speak or TTS not ready.", Toast.LENGTH_SHORT).show()
                    }*/
                },
                onMicClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, sourceLang.language.lowercase()) // Use source language for recognition
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to translate...")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        speechRecognizerLauncher.launch(intent)
                    } else {
                        Toast.makeText(context, "Speech input not supported on this device.", Toast.LENGTH_LONG).show()
                    }
                },
                onTranslateClick = { // Add a translate button or trigger on text change/mic input
                    viewModel.translateText(inputText)
                }
            )
        }

        // 3. Translation Output Card (only show if not loading and translatedText is not blank)
        //if (!isLoading && translatedText.isNotBlank()) {
            item {
                TranslationOutputCard(
                    languageLabel = targetLang.name,
                    translatedText = translatedText,
                    onSpeakOutput = {
                        viewModel.speakTranslatedText()
                        /*if (textToSpeech != null && translatedText.isNotBlank()) {
                            // Set language for output TTS based on targetLang
                            val locale = Locale(targetLang.language.lowercase())
                            val result = textToSpeech.setLanguage(locale)
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TranslatorScreen", "TTS language not supported: ${targetLang.language}")
                                Toast.makeText(context, "Language for speaking not supported: ${targetLang.name}", Toast.LENGTH_SHORT).show()
                            } else {
                                textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, "")
                                Toast.makeText(context, "Speaking translated text...", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "No translated text to speak or TTS not ready.", Toast.LENGTH_SHORT).show()
                        }*/
                    },
                    onCopyOutput = {
                        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Translated Text", translatedText)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, "Translated text copied!", Toast.LENGTH_SHORT).show()
                    },
                    onShareOutput = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, translatedText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share translated text"))
                    },
                    onFavoriteClick = {
                        // Implement favorite logic if needed
                        Toast.makeText(context, "Added to Favorites! (Not implemented)", Toast.LENGTH_SHORT).show()
                    }
                )
            }
       // }

    }

    if (showSourceLanguageDialog) {
        LanguageSelectionDialog(
            languages = languages, // Pass the list of fetched languages
            onLanguageSelected = { selectedLanguage ->
                viewModel.setSourceLanguage(selectedLanguage)
            },
            onDismiss = { viewModel.closeSourceLanguageDialog() },
            currentSelectedLanguageCode = sourceLang.language // Pass current code to highlight
        )
    }

    if (showTargetLanguageDialog) {
        LanguageSelectionDialog(
            languages = languages, // Pass the list of fetched languages
            onLanguageSelected = { selectedLanguage ->
                viewModel.setTargetLanguage(selectedLanguage)
            },
            onDismiss = { viewModel.closeTargetLanguageDialog() },
            currentSelectedLanguageCode = targetLang.language // Pass current code to highlight
        )
    }
}

