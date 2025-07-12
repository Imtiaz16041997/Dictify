package com.imtiaz.dictify.navigation.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.imtiaz.dictify.navigation.Screen
import com.imtiaz.dictify.navigation.currentRoute
import com.imtiaz.dictify.utils.singleTopNavigator

@Composable
fun BottomNavigationUI(navController: NavController, pagerState: PagerState) {
    NavigationBar {
        val items = when (pagerState.currentPage) {
            0 -> {
                listOf(
                    Screen.HomeNav,
                    Screen.BookmarksNav,
                    Screen.TranslatorNav,
                    Screen.ProfileNav,
                )
            }

            else -> {
                listOf(
                    Screen.HomeNav,
                    Screen.BookmarksNav,
                    Screen.TranslatorNav,
                    Screen.ProfileNav,
                )
            }
        }

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = item.navIcon,
                label = { Text(text = stringResource(id = item.title)) },
                selected = currentRoute(navController) == item.route,
                onClick = {
                    navController.singleTopNavigator(item.route)
                })
        }
    }
}
