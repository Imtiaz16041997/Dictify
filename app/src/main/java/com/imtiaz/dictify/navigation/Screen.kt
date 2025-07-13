package com.imtiaz.dictify.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import com.imtiaz.dictify.R

sealed class Screen(
    val route: String,
    @StringRes val titleResId: Int = R.string.app_name, // Renamed for clarity
    val icon: ImageVector? = null, // Store ImageVector, not a @Composable lambda
    val objectName: String = "",
    val objectPath: String = "" // For path arguments
) {
    // Bottom Navigation Destinations (these are the actual screen routes)
    data object Home : Screen("home_route", R.string.home, Icons.Filled.Home) // Add icons here
    data object Bookmarks : Screen("bookmarks_route", R.string.book_mark, Icons.Filled.Bookmarks)
    data object Translator : Screen("translator_route", R.string.translator, Icons.Filled.Translate)
    data object Profile : Screen("profile_route", R.string.profile, Icons.Filled.AccountBox)

    // Detail Screens or other specific routes
    // For WordDetail, specify the argument
    data object WordDetail : Screen(
        route = "word_detail_route/{wordItem}", // Define argument in the route
        titleResId = R.string.word_detail, // Assuming you have this string
        objectName = "wordItem" // Name of the argument
    ) {
        // Helper function to create the route with the actual word
        fun createRoute(wordItem: String): String {
            return "word_detail_route/${wordItem}"
        }
    }

}