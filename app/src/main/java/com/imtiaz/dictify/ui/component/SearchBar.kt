package com.imtiaz.dictify.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imtiaz.dictify.ui.theme.Cranberry


@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    onCloseClicked: () -> Unit,
    onMicClicked: () -> Unit,
    onLanguageClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(
                    0.1.dp,
                    SolidColor(MaterialTheme.colorScheme.onSurface)
                ),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Normal,
                )
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onBackground,
            ),
            singleLine = true,
            leadingIcon = {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle search icon click if needed */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Cranberry,
                            modifier = Modifier.size(22.dp)
                        )
                    }
//                    // Divider next to search icon with exact padding
//                    SearchBarDivider(
//                        modifier = Modifier
//                            .height(24.dp)
//                            .padding(start = 2.dp, end = 50.dp) // Maintain the 50.dp gap
//                    )
                }
            },
            trailingIcon = {
                // Group mic/clear icon, divider, and new language icon in a Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Divider (before mic/clear icon)
                    SearchBarDivider(
                        modifier = Modifier
                            .height(24.dp) // Match height
                            .padding(start = 50.dp, end = 2.dp) // Maintain the 50.dp gap
                    )

                    // Mic/Clear Icon
                    IconButton(onClick = {
                        if (text.isNotBlank()) {
                            onCloseClicked()
                        } else {
                            onMicClicked()
                        }
                    }) {
                        if (text.isNotBlank()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = Cranberry,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice search",
                                tint = Cranberry,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // New Language Icon
                    IconButton(onClick = onLanguageClicked) {
                        Icon(
                            imageVector = Icons.Default.Language, // Or Icons.Default.Translate
                            contentDescription = "Change Language",
                            tint = Cranberry,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
        )
        // Remove the standalone SearchBarDivider here, as it's now inside trailingIcon
/*
        SearchBarDivider(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 50.dp)
        )
*/
    }
}
