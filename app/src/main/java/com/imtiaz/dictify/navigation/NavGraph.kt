package com.imtiaz.dictify.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.imtiaz.dictify.R
import com.imtiaz.dictify.ui.screen.BookmarksScreen
import com.imtiaz.dictify.ui.screen.HomeScreen
import com.imtiaz.dictify.ui.screen.ProfileScreen
import com.imtiaz.dictify.ui.screen.TranslatorScreen

@Composable
fun Navigation(navController: NavHostController/*, genres: List<Genre>? = null,*/ ) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
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







