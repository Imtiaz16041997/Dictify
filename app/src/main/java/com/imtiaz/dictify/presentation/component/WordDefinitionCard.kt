package com.imtiaz.dictify.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtiaz.dictify.presentation.theme.light



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtiaz.dictify.presentation.theme.Cranberry

@Composable
fun WordDefinitionCard(
    word: String,
    definition: String,
    onRefreshClicked: () -> Unit,
    onCopyClicked: (String) -> Unit,
    onPronounceClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Cranberry)
            .padding(16.dp)
    ) {
        // Word and Pronunciation (if available)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = word,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { onPronounceClicked(word) }) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Pronounce",
                    tint = Color.Black, // Or MaterialTheme.colorScheme.primary
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Definition
        Text(
            text = definition,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onRefreshClicked) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Definition",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onCopyClicked(definition) }) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Definition",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}



//@Composable
//fun WordDefinitionCard(
//    modifier: Modifier = Modifier,
//    word: String,
//    definition: String,
//    onRefreshClicked: () -> Unit = {},
//    onCopyClicked: (String) -> Unit = {}, // Passes the word to be copied
//    onPronounceClicked: (String) -> Unit = {} // Passes the word to be pronounced
//) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(12.dp)), // Apply rounded corners to the card
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Add a subtle shadow
//        colors = CardDefaults.cardColors(containerColor = light) // Card background
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp) // Padding inside the card
//        ) {
//            // Top Row: Word Title and Action Buttons
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween // Pushes content to ends
//            ) {
//                // Word Title
//                Text(
//                    text = word,
//                    style = MaterialTheme.typography.headlineMedium, // Adjust typography as needed
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.weight(1f) // Makes text take available space, pushing icons right
//                )
//
//                // Action Buttons (Refresh, Copy, Pronounce)
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Refresh Button
//                    IconButton(onClick = onRefreshClicked) {
//                        Icon(
//                            imageVector = Icons.Default.Refresh, // or a custom refresh icon
//                            contentDescription = "Refresh Definition",
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant, // A muted color
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//
//                    // Copy Button
//                    IconButton(onClick = { onCopyClicked(word + "\n" + definition) }) { // Pass combined text
//                        Icon(
//                            imageVector = Icons.Default.ContentCopy, // Copy icon
//                            contentDescription = "Copy Definition",
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//
//                    // Pronunciation Button
//                    IconButton(onClick = { onPronounceClicked(word) }) { // Pass the word for pronunciation
//                        Icon(
//                            imageVector = Icons.Default.VolumeUp, // Volume icon for pronunciation
//                            contentDescription = "Pronounce Word",
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp)) // Space between title/buttons and description
//
//            // Description
//            Text(
//                text = definition,
//                style = MaterialTheme.typography.bodyMedium, // Adjust typography as needed
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewWordDefinitionCard() {
//    MaterialTheme { // Use your app's theme here
//        WordDefinitionCard(
//            word = "Emotions",
//            definition = "An emotion is a strong feeling, like the emotion you feel when you see your best friend at the movies with a group of people who cause trouble for you.",
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}