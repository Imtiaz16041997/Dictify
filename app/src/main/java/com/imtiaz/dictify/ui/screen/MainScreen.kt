package com.imtiaz.dictify.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.imtiaz.dictify.navigation.BottomNavigationUI
import com.imtiaz.dictify.navigation.Navigation
import com.imtiaz.dictify.navigation.Screen
import com.imtiaz.dictify.navigation.currentRoute
import com.imtiaz.dictify.navigation.navigationTitle
import com.imtiaz.dictify.ui.component.MySearchBar
import com.imtiaz.dictify.ui.component.WordDefinitionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val currentRoute = currentRoute(navController)

    val hideTopBarRoutes = listOf(Screen.WordDetail.route)
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Bookmarks.route,
        Screen.Translator.route,
        Screen.Profile.route,
    )
    val showTopAppBarActions = currentRoute !in hideTopBarRoutes

    val pagerState = rememberPagerState(initialPage = 0) {
        bottomNavRoutes.size
    }

    // State for the search bar, lifted to MainScreen
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (showTopAppBarActions) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary) // TopBar background color
                        .statusBarsPadding() // Handle system status bar insets
                ) {
                    // Top row for hamburger menu, title, and three dots
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp), // Adjust padding as needed
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // Distribute content
                    ) {
                        // Hamburger menu icon
                        IconButton(onClick = { /* Handle navigation drawer open */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        // Title
                        Text(
                            text = "iDictionary",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize // Adjust font size
                        )

                        // Three-dot menu icon
                        IconButton(onClick = { /* Handle more options */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.White
                            )
                        }
                    }

                    // Spacer for visual separation (optional, adjust height as needed)
                    Spacer(modifier = Modifier.height(8.dp))

                    // MySearchBar
                    // Padding for the search bar within the custom top bar
                    MySearchBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = searchText,
                        onTextChange = { newValue ->
                            searchText = newValue
                            // You might want to trigger search/filter here or in HomeScreen
                        },
                        placeholder = "Search word...",
                        onCloseClicked = {
                            searchText = ""
                        },
                        onMicClicked = {
                            // Handle mic click
                        }
                    )

                    // Padding below the search bar
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
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