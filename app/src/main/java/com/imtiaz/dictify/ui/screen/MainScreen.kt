package com.imtiaz.dictify.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController
import com.imtiaz.dictify.navigation.BottomNavigationUI
import com.imtiaz.dictify.navigation.Navigation
import com.imtiaz.dictify.navigation.Screen
import com.imtiaz.dictify.navigation.currentRoute
import com.imtiaz.dictify.navigation.navigationTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val isAppBarVisible = remember { mutableStateOf(true) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val currentRoute = currentRoute(navController)
    val pagerState = rememberPagerState { 1 }
    val hideTopBarRoutes = listOf(Screen.WordDetail.route)
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Bookmarks.route,
        Screen.Translator.route,
        Screen.Profile.route,
    )
    val showTopAppBarActions = currentRoute !in hideTopBarRoutes
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = navigationTitle(navController),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                },
                navigationIcon = {

                },
                actions = {
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationUI(navController, pagerState)
            }
        }
    ){ padding ->

        Box(Modifier.padding(padding)) {
            Navigation(navController = navController)
        }
    }
}