package com.imtiaz.dictify.presentation.component.dictionary


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtiaz.dictify.domain.model.dictionary.FavoriteWord
import com.imtiaz.dictify.presentation.theme.Cranberry


@Composable
fun FavoriteWordCard(
    favoriteWord: FavoriteWord,
    onDeleteClick: () -> Unit,
    onPronounceClick: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Cranberry) // Use surface color for card background
            .clickable { /* Optional: Navigate to detail screen */ }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = favoriteWord.word,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { onPronounceClick(favoriteWord.word) }) {
                Icon(
                    imageVector = Icons.Default.VolumeUp, // Use AutoMirrored if available
                    contentDescription = "Pronounce",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = { onCopyClick(favoriteWord.definition ?: "") }) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete from favorites",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = favoriteWord.definition ?: "No definition available.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.background,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFavoriteWordCard() {
    MaterialTheme { // Use your app's theme here for correct color resolution
        FavoriteWordCard(
            favoriteWord = FavoriteWord(
                word = "Ephemeral",
                definition = "Lasting for a very short time.",
                partOfSpeech = "adjective",
                audioUrl = "http://example.com/audio/ephemeral.mp3" // Dummy URL
            ),
            onDeleteClick = { /* Preview: Do nothing */ },
            onPronounceClick = { word -> /* Preview: Do nothing */ },
            onCopyClick = { text -> /* Preview: Do nothing */ },
            modifier = Modifier.padding(16.dp) // Add some padding for better preview visibility
        )
    }
}