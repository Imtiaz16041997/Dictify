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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WordExpandableDefinition(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    initialExpanded: Boolean = true // Whether it's expanded by default
) {
    // State to manage the expanded/collapsed state
    var expanded by remember { mutableStateOf(initialExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Apply rounded corners to the card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Add a subtle shadow
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Card background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 300)) // Smooth animation
        ) {
            // Header Row: Title and Expand/Collapse Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // Make the whole header clickable
                    .padding(16.dp), // Padding for the header
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp, // Adjust font size as needed
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f) // Takes available space
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
            // Collapsible Content
            if (expanded) {
                Text(
                    text = content,
                    fontSize = 16.sp, // Adjust font size as needed
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp) // Padding for content
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpandableDefinitionSection() {
    MaterialTheme { // Use your app's theme here
        Column(modifier = Modifier.padding(16.dp)) {
            WordExpandableDefinition(
                title = "Definition",
                content = "A word or phrase used to describe a thing or to express a concept, especially in a particular kind of language or branch of study. ♦ a fixed or limited period for which something, e.g., office, imprisonment, or investment, lasts or is intended to last. ♦ each of the periods in the year, alternating with holidays or vacations, during which instruction is given in a school, college, or university, or during which a court holds sessions. ♦ conditions under which an action may be undertaken or agreement reached; stipulated or agreed-upon requirements.",
                initialExpanded = true,
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}