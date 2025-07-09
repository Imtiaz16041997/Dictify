package com.imtiaz.dictify.navigation

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun Navigation(navController: NavHostController,  mainViewModel: MainViewModel/*, genres: List<Genre>? = null,*/ ) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController,mainViewModel)
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


//        composable(Screen.WordDetail.route + "/{word}",
//            arguments = listOf(navArgument("word") { type = NavType.StringType })) { backStackEntry ->
//            val word = backStackEntry.arguments?.getString("word")
//            // WordDetailScreen(word = word, viewModel = mainViewModel) // Or a dedicated detail ViewModel
//        }
    }
}

    //maps route → string resource for top bar

@Composable
fun navigationTitle(navController: NavController): String {
    return when (currentRoute(navController)) {
        else -> {
            stringResource(R.string.app_name)
        }
    }
}

    //It returns the screen’s current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}







