package com.imtiaz.dictify.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.imtiaz.dictify.R


data class BottomNavItem(
    val screen: Screen, // Reference to the actual screen
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null, // Optional: if you have different icons for selected state
    val contentDescription: String,
    val badgeCount: Int? = null // Optional: for notification badges
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Home,
        icon = Icons.Filled.Home,
        contentDescription = "Home"
    ),
    BottomNavItem(
        screen = Screen.Bookmarks,
        icon = Icons.Filled.Bookmarks,
        contentDescription = "Bookmarks"
    ),
    BottomNavItem(
        screen = Screen.Translator,
        icon = Icons.Filled.Translate,
        contentDescription = "Translator"
    ),
    BottomNavItem(
        screen = Screen.Profile,
        icon = Icons.Filled.AccountBox,
        contentDescription = "Profile"
    )
)


// It returns the screen’s current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route // Match exact route
}

// maps route → string resource for top bar
@Composable
fun navigationTitle(navController: NavController): String {
    val currentRoute = currentRoute(navController)
    return when (currentRoute) {
        Screen.Home.route -> stringResource(Screen.Home.titleResId)
        Screen.Bookmarks.route -> stringResource(Screen.Bookmarks.titleResId)
        Screen.Translator.route -> stringResource(Screen.Translator.titleResId)
        Screen.Profile.route -> stringResource(Screen.Profile.titleResId)
        Screen.WordDetail.route -> stringResource(Screen.WordDetail.titleResId)
        else -> stringResource(R.string.app_name) // Fallback for unknown routes
    }
}