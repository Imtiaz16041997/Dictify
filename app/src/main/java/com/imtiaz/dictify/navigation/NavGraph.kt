package com.imtiaz.dictify.navigation

import android.R.attr.type
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.imtiaz.dictify.R
import com.imtiaz.dictify.presentation.screen.BookmarksScreen
import com.imtiaz.dictify.presentation.screen.HomeScreen
import com.imtiaz.dictify.presentation.screen.ProfileScreen
import com.imtiaz.dictify.presentation.screen.TranslatorScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.MainViewModel

@Composable
fun Navigation(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.fillMaxSize(), // Important to ensure fills available space for transitions
        enterTransition = { // For when a screen enters
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Enters from right
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = { // For when a screen exits
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // Exits to left
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = { // For when a screen enters due to popping back stack
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // Enters from left
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = { // For when a screen exits due to popping back stack
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // Exits to right
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, mainViewModel)
        }
        composable(Screen.Bookmarks.route) {
            BookmarksScreen(navController)
        }
        composable(Screen.Translator.route) {
            TranslatorScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
    //maps route → string resource for top bar

@Composable
fun navigationTitle(navController: NavController): String {
    return when (currentRoute(navController)) {
        Screen.Home.route -> stringResource(R.string.app_name) // Assuming R.string.app_name is "iDictionary"
        Screen.Bookmarks.route -> stringResource(R.string.app_name) // Or stringResource(R.string.bookmarks_title)
        Screen.Translator.route -> stringResource(R.string.app_name) // Or stringResource(R.string.translator_title)
        Screen.Profile.route -> stringResource(R.string.app_name)// Or stringResource(R.string.profile_title)
        else -> stringResource(R.string.app_name) // Default or fallback
    }
}

    //It returns the screen’s current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}







