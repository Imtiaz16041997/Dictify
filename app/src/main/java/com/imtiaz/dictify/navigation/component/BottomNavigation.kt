package com.imtiaz.dictify.navigation.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.imtiaz.dictify.R
import com.imtiaz.dictify.navigation.Screen
import com.imtiaz.dictify.navigation.bottomNavItems
import com.imtiaz.dictify.navigation.currentRoute
import com.imtiaz.dictify.utils.singleTopNavigator

@Composable
fun BottomNavigationUI(navController: NavController, pagerState: PagerState) { // pagerState seems unused here?
    NavigationBar {
        // Use the centralized list of bottom navigation items
        val items = bottomNavItems // From navigation/BottomNavItem.kt

        val currentDestinationRoute = currentRoute(navController)

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.contentDescription) },
                label = { Text(text = stringResource(id = R.string.app_name)) }, // Use titleResId from Screen
                selected = currentDestinationRoute == item.screen.route,
                onClick = {
                    // **Solution for Continuous Tapping (Debouncing Nav)**
                    // Only navigate if the current route is different from the target route
                    if (currentDestinationRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            // Pop up to the start destination of the graph to avoid building up a large backstack
                            // This pops everything except the start destination of your main graph.
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true // Save the state of the popped-up destination
                            }
                            // Avoid multiple copies of the same destination when reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
