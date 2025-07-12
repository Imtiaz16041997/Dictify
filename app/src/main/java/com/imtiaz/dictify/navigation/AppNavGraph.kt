package com.imtiaz.dictify.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.imtiaz.dictify.presentation.screen.mainscreen.BookmarksScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.HomeScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.ProfileScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.TranslatorScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.MainViewModel
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavType
import androidx.navigation.navArgument


@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel, // Passed down if needed by HomeScreen
    modifier: Modifier = Modifier // Allow modifier to be passed from parent
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // Start with the home route
        modifier = modifier.fillMaxSize(),
        // Apply transitions for smoother navigation (and flicker reduction)
 /*       enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { -it / 2 } },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { -it / 2 } },
        popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 } }
 */
        ) {
        composable(Screen.Home.route) { backStackEntry ->
            HomeScreen(navController, mainViewModel)
        }
        composable(Screen.Bookmarks.route) { backStackEntry ->
            BookmarksScreen(navController)
        }
        composable(Screen.Translator.route) { backStackEntry ->
            TranslatorScreen(navController)
        }
        composable(Screen.Profile.route) { backStackEntry ->
            ProfileScreen(navController)
        }

        // WordDetail with argument
        composable(
            route = Screen.WordDetail.route, // Use the route with argument
            arguments = listOf(navArgument(Screen.WordDetail.objectName) {
                // You can specify type, nullable, defaultValue etc.
                type = NavType.StringType // Assuming the word is a String
                nullable = true // If word can be null or empty
            })
        ) { backStackEntry ->
            val wordItem = backStackEntry.arguments?.getString(Screen.WordDetail.objectName)
            //WordDetailScreen(navController, wordItem = wordItem) // Pass the word to the detail screen
        }

    }
}





