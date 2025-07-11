package com.imtiaz.dictify.presentation.screen


import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtiaz.dictify.R
import java.util.Locale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.component.translator.LanguageSelector
import com.imtiaz.dictify.presentation.component.translator.TranslationInputCard
import com.imtiaz.dictify.presentation.component.translator.TranslationOutputCard
import com.imtiaz.dictify.presentation.screen.mainscreen.translatorviewmodel.TranslatorViewModel


@Composable
fun TranslatorScreen(
    navController: NavController,
    viewModel: TranslatorViewModel = hiltViewModel()

) {

    // Observe languages from the ViewModel
    val languages by viewModel.languages.collectAsState()
    val sourceLang by viewModel.sourceLanguage.collectAsState()
    val targetLang by viewModel.targetLanguage.collectAsState()
    // For now, let's use local states. In a real app, these would come from a TranslatorViewModel.
    //var sourceLang by remember { mutableStateOf("English") }
    //var targetLang by remember { mutableStateOf("Spanish") }
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // For translation loading

    val context = LocalContext.current

    // TextToSpeech for input and output pronunciation
    val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        lateinit var ttsInstance: TextToSpeech
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance.language = Locale.US // Default for English input/output
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
    }
    val textToSpeech = textToSpeechState.value

    // Speech Recognition Launcher for input mic
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenTexts = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenTexts?.get(0) ?: ""
            if (spokenText.isNotBlank()) {
                inputText = spokenText // Set input text from voice
                // Trigger translation immediately after voice input
                // This would be viewModel.translate(inputText, sourceLang, targetLang)
                translatedText = "Translating: \"$spokenText\"..." // Placeholder
                isLoading = true // Show loading
                // Simulate API call
                // CoroutineScope(Dispatchers.IO).launch {
                //     delay(1000)
                //     translatedText = "Translated: \"$spokenText\""
                //     isLoading = false
                // }
            }
        } else {
            Toast.makeText(context, "Voice input cancelled/failed.", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Space between cards
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Language Selector
        item {
           /* LanguageSelector(
                sourceLanguage = sourceLang,
                sourceFlag = painterResource(id = R.drawable.ic_flag_us), // Replace with your actual US flag drawable
                targetLanguage = targetLang,
                targetFlag = painterResource(id = R.drawable.ic_flag_es), // Replace with your actual ES flag drawable
                onSourceClick = { Toast.makeText(context, "Select Source Language", Toast.LENGTH_SHORT).show() },
                onTargetClick = { Toast.makeText(context, "Select Target Language", Toast.LENGTH_SHORT).show() },
                onSwapClick = {
                    val temp = sourceLang
                    sourceLang = targetLang
                    targetLang = temp
                    // Also swap translated text if any
                    val tempText = inputText
                    inputText = translatedText
                    translatedText = tempText
                    Toast.makeText(context, "Languages Swapped!", Toast.LENGTH_SHORT).show()
                }
            )*/
            LanguageSelector(
                sourceLanguage = sourceLang.name, // Use the name from the state
                sourceFlag = painterResource(id = R.drawable.ic_flag_us),
                targetLanguage = targetLang.name, // Use the name from the state
                targetFlag = painterResource(id = R.drawable.ic_flag_es),
                onSourceClick = {
                    // Show a language selection dialog/bottom sheet with `languages` list
                    // and call viewModel.setSourceLanguage(selectedLanguage)
                },
                onTargetClick = {
                    // Show a language selection dialog/bottom sheet with `languages` list
                    // and call viewModel.setTargetLanguage(selectedLanguage)
                },
                onSwapClick = { viewModel.swapLanguages() }
            )

        }

        // 2. Translation Input Card
        item {
           /* TranslationInputCard(
                languageLabel = sourceLang,
                inputText = inputText,
                onInputTextChange = { inputText = it },
                onClearInput = { inputText = "" },
                onSpeakInput = {
                    if (textToSpeech != null && inputText.isNotBlank()) {
                        textToSpeech.speak(inputText, TextToSpeech.QUEUE_FLUSH, null, "")
                        Toast.makeText(context, "Speaking input...", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No text to speak or TTS not ready.", Toast.LENGTH_SHORT).show()
                    }
                },
                onMicClick = {
                    // Launch speech recognition
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to translate...")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        speechRecognizerLauncher.launch(intent)
                    } else {
                        Toast.makeText(context, "Speech input not supported.", Toast.LENGTH_LONG).show()
                    }
                },
                onTranslateClick = {
                    if (inputText.isNotBlank()) {
                        isLoading = true
                        translatedText = "Translating..."
                        // In a real app, call your ViewModel's translate function here
                        // viewModel.translate(inputText, sourceLang, targetLang)
                        // For demo:
                        // CoroutineScope(Dispatchers.IO).launch {
                        //     delay(1500)
                        //     translatedText = "Translated: ${inputText.reversed()}" // Dummy translation
                        //     isLoading = false
                        // }
                        Toast.makeText(context, "Translating...", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please enter text to translate.", Toast.LENGTH_SHORT).show()
                    }
                }
            )*/
        }

//        // Loading indicator (conditionally displayed)
//        if (isLoading) {
//            item {
//                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//            }
//        }

        // 3. Translation Output Card (only show if not loading and translatedText is not blank)
//        if (!isLoading && translatedText.isNotBlank()) {
            item {
                /*TranslationOutputCard(
                    languageLabel = targetLang,
                    translatedText = translatedText,
                    onSpeakOutput = {
                        if (textToSpeech != null && translatedText.isNotBlank()) {
                            // Set language for output TTS based on targetLang
                            textToSpeech.language = Locale(targetLang.substring(0, 2).lowercase()) // e.g., "es" for Spanish
                            textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, "")
                            Toast.makeText(context, "Speaking translated text...", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No translated text to speak or TTS not ready.", Toast.LENGTH_SHORT).show()
                        }
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
                        Toast.makeText(context, "Added to Favorites!", Toast.LENGTH_SHORT).show()
                    }
                )*/
            }
        //}
    }
}