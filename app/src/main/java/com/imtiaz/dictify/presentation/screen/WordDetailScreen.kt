package com.imtiaz.dictify.presentation.screen


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.MainViewModel

@Composable
fun WordDetailScreen(
    navController: NavController, // Passed from AppNavGraph
    wordItem: String,             // Passed from AppNavGraph
    mainViewModel: MainViewModel, // ADDED: Now matches AppNavGraph call
    onBack: () -> Unit            // Passed from AppNavGraph
) {
    // ... your WordDetailScreen implementation using these parameters
    // Example: Text("Word Detail for: $wordItem")
    // Use mainViewModel to fetch details if needed, or to share state.
}