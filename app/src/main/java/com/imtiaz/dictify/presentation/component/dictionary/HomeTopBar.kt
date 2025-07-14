package com.imtiaz.dictify.presentation.component.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopBar(
    searchText: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onMicClicked: () -> Unit,
    onLanguageClicked: () -> Unit,
    onSearchTriggered: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
           /* .background(Color.Black) // top bar color*/
            //.statusBarsPadding()
    ) {
        /*Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { *//* Handle navigation drawer open *//* }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
            Text(
                text = "iDictionary",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
            IconButton(onClick = { *//* Handle more options *//* }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
        }*/

        //Spacer(modifier = Modifier.height(8.dp))

        MySearchBar( // Assuming MySearchBar is a custom composable you have
            modifier = Modifier.padding(2.dp),
            text = searchText,
            onTextChange = onTextChange,
            placeholder = "Search word...",
            onCloseClicked = onCloseClicked,
            onMicClicked = onMicClicked,
            onLanguageClicked = onLanguageClicked,
            onSearchTriggered = onSearchTriggered
        )

        //Spacer(modifier = Modifier.height(8.dp))
    }
}
