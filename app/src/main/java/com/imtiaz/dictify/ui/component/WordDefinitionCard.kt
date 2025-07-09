package com.imtiaz.dictify.ui.component

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
import com.imtiaz.dictify.ui.theme.light


@Composable
fun WordDefinitionCard(
    modifier: Modifier = Modifier,
    word: String,
    definition: String,
    onRefreshClicked: () -> Unit = {},
    onCopyClicked: (String) -> Unit = {}, // Passes the word to be copied
    onPronounceClicked: (String) -> Unit = {} // Passes the word to be pronounced
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Apply rounded corners to the card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Add a subtle shadow
        colors = CardDefaults.cardColors(containerColor = light) // Card background
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Padding inside the card
        ) {
            // Top Row: Word Title and Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Pushes content to ends
            ) {
                // Word Title
                Text(
                    text = word,
                    style = MaterialTheme.typography.headlineMedium, // Adjust typography as needed
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Makes text take available space, pushing icons right
                )

                // Action Buttons (Refresh, Copy, Pronounce)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Refresh Button
                    IconButton(onClick = onRefreshClicked) {
                        Icon(
                            imageVector = Icons.Default.Refresh, // or a custom refresh icon
                            contentDescription = "Refresh Definition",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant, // A muted color
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Copy Button
                    IconButton(onClick = { onCopyClicked(word + "\n" + definition) }) { // Pass combined text
                        Icon(
                            imageVector = Icons.Default.ContentCopy, // Copy icon
                            contentDescription = "Copy Definition",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Pronunciation Button
                    IconButton(onClick = { onPronounceClicked(word) }) { // Pass the word for pronunciation
                        Icon(
                            imageVector = Icons.Default.VolumeUp, // Volume icon for pronunciation
                            contentDescription = "Pronounce Word",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Space between title/buttons and description

            // Description
            Text(
                text = definition,
                style = MaterialTheme.typography.bodyMedium, // Adjust typography as needed
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordDefinitionCard() {
    MaterialTheme { // Use your app's theme here
        WordDefinitionCard(
            word = "Emotions",
            definition = "An emotion is a strong feeling, like the emotion you feel when you see your best friend at the movies with a group of people who cause trouble for you.",
            modifier = Modifier.padding(16.dp)
        )
    }
}