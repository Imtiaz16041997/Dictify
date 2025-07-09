package com.imtiaz.dictify.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imtiaz.dictify.ui.component.WordDefinitionCard


@Composable
fun HomeScreen(navController: NavController) {
    // searchText state is managed in MainScreen, then passed down via a lambda
    // and lifted up when changed. It should not be managed here if MySearchBar is in TopBar.
     //var searchText by remember { mutableStateOf("") } // Remove this from HomeScreen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Changed to background color for overall screen
    ) {
        // MySearchBar is now in MainScreen's topBar slot, so remove it from HomeScreen
       /* MySearchBar(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = searchText,
            onTextChange = { newValue ->
                searchText = newValue
            },
            placeholder = "Search word...",
            onCloseClicked = {
                searchText = ""
            },
            onMicClicked = {
                // Handle mic click
            }
        )*/
        Spacer(modifier = Modifier.height(8.dp))
        WordDefinitionCard(
            word = "Emotions",
            definition = "An emotion is a strong feeling, like the emotion you feel when you see your best friend at the movies with a group of people who cause trouble for you.",
            onRefreshClicked = {
                //  refresh action is for random word
                println("Refresh clicked!")
            },
            onCopyClicked = { textToCopy ->
                println("Copied: $textToCopy")
            },
            onPronounceClicked = { wordToPronounce ->
                // will Implement text-to-speech
                println("Pronounce: $wordToPronounce")
            },
            modifier = Modifier.padding(horizontal = 16.dp) // Horizontal padding for the card
        )


        //These segment will add in more which is now profile
//        val features = listOf(
//            "Camera" to Icons.Default.Camera,
//            "Translate" to Icons.Default.Translate,
//            "Conversation" to Icons.Default.Chat,
//            "Grammar" to Icons.Default.Spellcheck,
//            "Phrases" to Icons.Default.List,
//            "Quiz" to Icons.Default.QuestionAnswer,
//            "Vocabulary" to Icons.Default.MenuBook,
//            "Idioms" to Icons.Default.Textsms,
//            "My Word" to Icons.Default.Star
//        )
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3),
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(items = features) { (label, icon) ->
//                Column(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(MaterialTheme.colorScheme.primaryContainer)
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                        .aspectRatio(1f),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(imageVector = icon, contentDescription = label)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(text = label, fontSize = 14.sp, textAlign = TextAlign.Center)
//                }
//            }
//        }
    }
}
