package com.imtiaz.dictify.presentation.component.random

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtiaz.dictify.data.local.entity.DailyWordEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailyWordCard(
    dailyWord: DailyWordEntity,
    onPronounceClick: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onCardClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // Use surface color for cards
            .padding(16.dp)
            // Add a clickable modifier to handle onCardClick
            .clickable { onCardClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = dailyWord.word.capitalize(Locale.getDefault()),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = dailyWord.definition ?: "No definition available.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        dailyWord.partOfSpeech?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        Text(
            text = "Fetched on: ${dailyWord.fetchedDate?.let { dateFormatter.format(Date(it)) } ?: "N/A"}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        // Row for actions (pronounce, copy)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { onPronounceClick(dailyWord.word) }) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Pronounce", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = { onCopyClick(dailyWord.definition ?: dailyWord.word) }) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

//You might want a slightly different card for daily words vs favorite words