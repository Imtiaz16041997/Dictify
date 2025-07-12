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
        modifier = Modifier.fillMaxSize(),

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
        Screen.Home.route -> stringResource(R.string.app_name)
        Screen.Bookmarks.route -> stringResource(R.string.app_name)
        Screen.Translator.route -> stringResource(R.string.app_name)
        Screen.Profile.route -> stringResource(R.string.app_name)
        else -> stringResource(R.string.app_name)
    }
}

    //It returns the screen’s current route
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}







