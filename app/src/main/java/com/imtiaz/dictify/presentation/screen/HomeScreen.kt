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
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.presentation.screen.mainscreen.MainViewModel
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    // ViewModel is provided by Hilt
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Initialize TextToSpeech once and remember it
    val textToSpeech by remember {
        mutableStateOf(
            TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    //textToSpeech.language = Locale.US // Set default language
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // Adjust for spacing above the first item, potentially from TopBar
        item {
            // This spacer might need to be removed or adjusted depending on your Scaffold padding
            // It was previously used to move content up, but Scaffold content padding handles this.
            // If you still have visual issues, adjust this.
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- Dictionary Search Result Display ---
        if (uiState.isLoading && uiState.wordDefinition == null && uiState.error == null) {
            // Show a loading indicator specifically for the dictionary card area
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Give it a fixed height for loading placeholder
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)), // Lighter background for loading
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (uiState.error != null) {
            // Show error message
            item {
                Text(
                    text = "Error: ${uiState.error?.localizedMessage ?: "Unknown error"}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (uiState.wordDefinition != null) {
            // Display WordDefinitionCard if a definition is available and not loading
            item {
                WordDefinitionCard(
                    word = uiState.wordDefinition!!.word,
                    definition = uiState.wordDefinition!!.definition,
                    // You might add pronunciation field to WordsInformation model if available
                    onRefreshClicked = {
                        // Re-lookup the current word (e.g., if user clicked refresh)
                        uiState.wordDefinition?.word?.let { word ->
                            viewModel.lookupWordDefinition(word)
                        }
                    },
                    onCopyClicked = { textToCopy ->
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Dictionary Definition", textToCopy)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    onPronounceClicked = { wordToPronounce ->
                        textToSpeech.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
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
