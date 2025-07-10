package com.imtiaz.dictify.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background




@Composable
fun WordExpandableDefinition(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    headerBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    initialExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(initialExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // **FIX**: Set the Card's containerColor to match the headerBackgroundColor.
        // This ensures the whole card area has a consistent background, even when collapsed.
        colors = CardDefaults.cardColors(containerColor = headerBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 300))
        ) {
            // Header Row: Title and Expand/Collapse Icon with its own background
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // No need to apply background here if Card's containerColor handles it
                    // .background(headerBackgroundColor) // <-- REMOVE OR COMMENT THIS LINE
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Collapsible Content with its own background
            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(contentBackgroundColor) // Apply content background color here
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = content,
                        fontSize = 16.sp,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWordExpandableDefinitionWithBackgrounds() {
    MaterialTheme { // Use your app's theme here
        Column(modifier = Modifier.padding(16.dp)) {
            WordExpandableDefinition(
                title = "Definition",
                content = "A word or phrase used to describe a thing or to express a concept...",
                initialExpanded = true,
                titleColor = Color.White, // Example title color
                contentColor = Color.LightGray, // Example content text color
                headerBackgroundColor = Color(0xFF6200EE), // Deep Purple for header
                contentBackgroundColor = Color(0xFF3700B3), // Darker Purple for content
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}