package com.imtiaz.dictify.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.imtiaz.dictify.presentation.screen.mainscreen.BookmarksScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.HomeScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.HistoryScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.TranslatorScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.MainViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.imtiaz.dictify.presentation.screen.WordDetailScreen
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.BookmarkViewModel
import com.imtiaz.dictify.presentation.screen.mainscreen.history.HistoryViewModel


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
            HistoryScreen (navController)
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





