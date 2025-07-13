package com.imtiaz.dictify.presentation.component.translator


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imtiaz.dictify.R // Assuming you have R for drawable resources (flags)

@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    sourceLanguage: String,
    sourceFlag: Painter,
    targetLanguage: String,
    targetFlag: Painter,
    onSourceClick: () -> Unit,
    onTargetClick: () -> Unit,
    onSwapClick: () -> Unit
) {
    Card( // <--- NOW A CARD
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Clip the Card itself for rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // A subtle elevation for separation
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Use surface color
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Source Language (e.g., English)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onSourceClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = sourceFlag,
                    contentDescription = "$sourceLanguage Flag",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sourceLanguage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Swap Icon
            IconButton(
                onClick = onSwapClick,
                modifier = Modifier.size(48.dp) // Make icon button a bit larger for easy tap
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Swap Languages",
                    tint = MaterialTheme.colorScheme.primary // Use primary color for accent
                )
            }

            // Target Language (e.g., Spanish)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onTargetClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = targetFlag,
                    contentDescription = "$targetLanguage Flag",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = targetLanguage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLanguageSelector() {
    MaterialTheme {
        LanguageSelector(
            sourceLanguage = "English",
            sourceFlag = painterResource(id = R.drawable.ic_flag_ussss), // Replace with your actual drawable
            targetLanguage = "Spanish",
            targetFlag = painterResource(id = R.drawable.ic_flag_ess), // Replace with your actual drawable
            onSourceClick = {},
            onTargetClick = {},
            onSwapClick = {}
        )
    }
}