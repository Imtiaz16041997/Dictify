package com.imtiaz.dictify.presentation.component.dictionary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.imtiaz.dictify.presentation.theme.Cranberry


@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    onCloseClicked: () -> Unit,
    onMicClicked: () -> Unit,
    onLanguageClicked: () -> Unit,
    onSearchTriggered: (String) -> Unit // <--- NEW PARAMETER
) {
    val focusManager = LocalFocusManager.current // To clear focus on search

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(
                    0.1.dp,
                    SolidColor(MaterialTheme.colorScheme.background)
                ),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it) // Only update the text state, don't trigger API here
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // <--- Show search icon on keyboard
            ),
            keyboardActions = KeyboardActions(
                onSearch = { // <--- Trigger search when keyboard search button is pressed
                    onSearchTriggered(text)
                    focusManager.clearFocus() // Hide keyboard
                }
            ),
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // This IconButton will now trigger the search
                    IconButton(onClick = {
                        onSearchTriggered(text) // <--- Trigger search when search icon is clicked
                        focusManager.clearFocus() // Hide keyboard
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Cranberry,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Divider (before mic/clear icon)
                    SearchBarDivider(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(start = 50.dp, end = 2.dp)
                    )

                    // Mic/Clear Icon
                    IconButton(onClick = {
                        if (text.isNotBlank()) {
                            onCloseClicked()
                            focusManager.clearFocus() // Hide keyboard on clear
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
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language",
                            tint = Cranberry,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
        )
    }
}

