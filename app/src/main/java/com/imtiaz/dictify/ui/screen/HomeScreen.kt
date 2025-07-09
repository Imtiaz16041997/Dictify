package com.imtiaz.dictify.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imtiaz.dictify.ui.component.MySearchBar


@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {

        MySearchBar(
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

            }
        )


        val features = listOf(
        "Camera" to Icons.Default.Camera,
        "Translate" to Icons.Default.Translate,
        "Conversation" to Icons.Default.Chat,
        "Grammar" to Icons.Default.Spellcheck,
        "Phrases" to Icons.Default.List,
        "Quiz" to Icons.Default.QuestionAnswer,
        "Vocabulary" to Icons.Default.MenuBook,
        "Idioms" to Icons.Default.Textsms,
        "My Word" to Icons.Default.Star
    )

        //  Feature Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = features) { (label, icon) ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = icon, contentDescription = label)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = label, fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}




