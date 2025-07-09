package com.imtiaz.dictify.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchBarDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.height(24.dp).width(1.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )
}