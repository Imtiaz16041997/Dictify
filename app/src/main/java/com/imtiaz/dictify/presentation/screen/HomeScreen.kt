package com.imtiaz.dictify.presentation.screen

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
import com.imtiaz.dictify.presentation.component.WordDefinitionCard
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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.component.WordExpandableDefinition
import com.imtiaz.dictify.presentation.screen.mainscreen.MainViewModel
import com.imtiaz.dictify.presentation.theme.PurpleGrey40
import java.util.Locale


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Use a nullable state to hold the TextToSpeech instance
    val textToSpeechState = remember { mutableStateOf<TextToSpeech?>(null) }

    // Use DisposableEffect to initialize and clean up TextToSpeech
    DisposableEffect(context) {
        // Declare a variable to hold the TextToSpeech instance
        // This will be assigned after construction
        lateinit var ttsInstance: TextToSpeech

        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Now that `ttsInstance` has been assigned, we can set its language.
                // This callback is executed *after* TextToSpeech constructor finishes.
                ttsInstance.language = Locale.US
            } else {
                Log.e("HomeScreen", "TextToSpeech initialization failed: $status")
            }
        }

        // Create the TextToSpeech instance, passing the listener
        ttsInstance = TextToSpeech(context, listener)

        // Assign the newly created and initializing TTS instance to our state
        textToSpeechState.value = ttsInstance

        // Clean up TextToSpeech when the composable leaves the composition
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
            Log.d("HomeScreen", "TextToSpeech shutdown.")
        }
    }

    // Get the actual TextToSpeech instance from the state.
    // It can be null if initialization hasn't completed or failed.
    val textToSpeech = textToSpeechState.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey40),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Get the first word definition if available
        // The API returns a list, so we'll display the first one in the card
        val firstWordDefinition = uiState.wordDefinition.firstOrNull()

        // --- Dictionary Search Result Display ---
        // Condition for loading: isLoading is true AND there are no current results AND no error
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
                Text(
                    text = "Error: ${uiState.error?.localizedMessage ?: "No definition found. Try another word."}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (firstWordDefinition != null) { // Display if at least one definition is found
            item {
                WordDefinitionCard(
                    word = firstWordDefinition.word.toString(),
                    // Access the definition from the first meaning, first definition, or provide a default
                    definition = firstWordDefinition.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition
                        ?: "Definition not available.",
                    onRefreshClicked = {
                        // Re-lookup the current word (e.g., if user clicked refresh)
                        firstWordDefinition.word.let { word ->
                            viewModel.lookupWordDefinition(word.toString())
                        }
                    },
                    onCopyClicked = { textToCopy ->
                        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Dictionary Definition", textToCopy)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    onPronounceClicked = { wordToPronounce ->
                        // Attempt to use audio URL first if available
                        val audioUrl = firstWordDefinition.phonetics?.firstOrNull { it.audio?.isNotBlank() == true }?.audio
                        if (audioUrl != null) {
                            // TODO: Implement audio playback from URL (e.g., using Android MediaPlayer)
                            // For now, fall back to TextToSpeech
                            textToSpeech?.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")

                        } else {
                            textToSpeech?.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")

                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                WordExpandableDefinition(
                    title = firstWordDefinition.meanings?.firstOrNull()?.definitions.toString() ?: "Definition not available.",
                    content = firstWordDefinition.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition
                        ?: "Definition not available.",
                        initialExpanded = true,
                        modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}


//@Composable
//fun HomeScreen(navController: NavController) {
//    // searchText state is managed in MainScreen, then passed down via a lambda
//    // and lifted up when changed. It should not be managed here if MySearchBar is in TopBar.
//     //var searchText by remember { mutableStateOf("") } // Remove this from HomeScreen
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background) // Changed to background color for overall screen
//    ) {
//        // MySearchBar is now in MainScreen's topBar slot, so remove it from HomeScreen
//       /* MySearchBar(
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//            text = searchText,
//            onTextChange = { newValue ->
//                searchText = newValue
//            },
//            placeholder = "Search word...",
//            onCloseClicked = {
//                searchText = ""
//            },
//            onMicClicked = {
//                // Handle mic click
//            }
//        )*/
//        Spacer(modifier = Modifier.height(8.dp))
//        WordDefinitionCard(
//            word = "Emotions",
//            definition = "An emotion is a strong feeling, like the emotion you feel when you see your best friend at the movies with a group of people who cause trouble for you.",
//            onRefreshClicked = {
//                //  refresh action is for random word
//                println("Refresh clicked!")
//            },
//            onCopyClicked = { textToCopy ->
//                println("Copied: $textToCopy")
//            },
//            onPronounceClicked = { wordToPronounce ->
//                // will Implement text-to-speech
//                println("Pronounce: $wordToPronounce")
//            },
//            modifier = Modifier.padding(horizontal = 16.dp) // Horizontal padding for the card
//        )
//
//
//        //These segment will add in more which is now profile
////        val features = listOf(
////            "Camera" to Icons.Default.Camera,
////            "Translate" to Icons.Default.Translate,
////            "Conversation" to Icons.Default.Chat,
////            "Grammar" to Icons.Default.Spellcheck,
////            "Phrases" to Icons.Default.List,
////            "Quiz" to Icons.Default.QuestionAnswer,
////            "Vocabulary" to Icons.Default.MenuBook,
////            "Idioms" to Icons.Default.Textsms,
////            "My Word" to Icons.Default.Star
////        )
////
////        LazyVerticalGrid(
////            columns = GridCells.Fixed(3),
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(16.dp),
////            verticalArrangement = Arrangement.spacedBy(12.dp),
////            horizontalArrangement = Arrangement.spacedBy(12.dp)
////        ) {
////            items(items = features) { (label, icon) ->
////                Column(
////                    modifier = Modifier
////                        .clip(RoundedCornerShape(16.dp))
////                        .background(MaterialTheme.colorScheme.primaryContainer)
////                        .padding(16.dp)
////                        .fillMaxWidth()
////                        .aspectRatio(1f),
////                    horizontalAlignment = Alignment.CenterHorizontally,
////                    verticalArrangement = Arrangement.Center
////                ) {
////                    Icon(imageVector = icon, contentDescription = label)
////                    Spacer(modifier = Modifier.height(8.dp))
////                    Text(text = label, fontSize = 14.sp, textAlign = TextAlign.Center)
////                }
////            }
////        }
//    }
//}
