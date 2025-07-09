package com.imtiaz.dictify.presentation.screen.mainscreen

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
import com.imtiaz.dictify.presentation.component.MySearchBar


import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController






@Composable
fun MainScreen(){
    val navController = rememberNavController()
    // val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()) // Not used directly on Scaffold, but kept if you integrate it
    val currentRoute = currentRoute(navController)

    val hideTopBarRoutes = listOf(Screen.WordDetail.route) // Add other routes where top bar should be hidden
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Bookmarks.route,
        Screen.Translator.route,
        Screen.Profile.route,
    )
    val showTopAppBarActions = currentRoute !in hideTopBarRoutes

    // For rememberPagerState: Ensure you have the necessary dependency.
    // If you're on Compose Foundation 1.6.0+ it's built-in.
    // Otherwise, you might need accompanist-pager.
    val pagerState = rememberPagerState(initialPage = 0) {
        bottomNavRoutes.size
    }

    // Get the ViewModel here as it's the bridge to the data
    val mainViewModel: MainViewModel = hiltViewModel()

    // State for the search bar, lifted to MainScreen to control it globally
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (showTopAppBarActions) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary) // Your custom TopBar background
                        .statusBarsPadding() // Handle system status bar insets
                ) {
                    // Top row for hamburger menu, title, and three dots
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
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
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
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

                    Spacer(modifier = Modifier.height(8.dp)) // Spacer below title bar

                    // MySearchBar
                    MySearchBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = searchText,
                        onTextChange = { newValue ->
                            searchText = newValue
                            // Trigger the dictionary lookup from the ViewModel
                            // This will now exclusively trigger dictionary search.
                            // If you have multiple search contexts (e.g., movie search on another tab),
                            // you'd add logic here to check the active tab/context.
                            mainViewModel.lookupWordDefinition(newValue)
                        },
                        placeholder = "Search word...",
                        onCloseClicked = {
                            searchText = ""
                            // Clear the current definition in ViewModel when search is cleared
                            mainViewModel.lookupWordDefinition("")
                        },
                        onMicClicked = {
                            println("Mic icon clicked!")
                            // Implement voice input
                        },
                        onLanguageClicked = {
                            println("Language icon clicked!")
                            // Implement language selection for translation API
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // Spacer below search bar
                }
            }
        },
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationUI(navController, pagerState)
            }
        }
    ){ paddingValues -> // Use paddingValues provided by Scaffold
        Box(Modifier.padding(paddingValues)) {
            // Pass the ViewModel instance to Navigation/HomeScreen
            Navigation(navController = navController, mainViewModel = mainViewModel)
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen(){
//    val navController = rememberNavController()
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//    val currentRoute = currentRoute(navController)
//
//    val hideTopBarRoutes = listOf(Screen.WordDetail.route)
//    val bottomNavRoutes = listOf(
//        Screen.Home.route,
//        Screen.Bookmarks.route,
//        Screen.Translator.route,
//        Screen.Profile.route,
//    )
//    val showTopAppBarActions = currentRoute !in hideTopBarRoutes
//
//    val pagerState = rememberPagerState(initialPage = 0) {
//        bottomNavRoutes.size
//    }
//
//    // State for the search bar, lifted to MainScreen
//    var searchText by remember { mutableStateOf("") }
//
//    Scaffold(
//        topBar = {
//            if (showTopAppBarActions) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.primary) // TopBar background color
//                        .statusBarsPadding() // Handle system status bar insets
//                ) {
//                    // Top row for hamburger menu, title, and three dots
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 4.dp), // Adjust padding as needed
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween // Distribute content
//                    ) {
//                        // Hamburger menu icon
//                        IconButton(onClick = { /* Handle navigation drawer open */ }) {
//                            Icon(
//                                imageVector = Icons.Default.Menu,
//                                contentDescription = "Menu",
//                                tint = Color.White
//                            )
//                        }
//
//                        // Title
//                        Text(
//                            text = "iDictionary",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = MaterialTheme.typography.titleLarge.fontSize // Adjust font size
//                        )
//
//                        // Three-dot menu icon
//                        IconButton(onClick = { /* Handle more options */ }) {
//                            Icon(
//                                imageVector = Icons.Default.MoreVert,
//                                contentDescription = "More options",
//                                tint = Color.White
//                            )
//                        }
//                    }
//
//                    // Spacer for visual separation (optional, adjust height as needed)
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // MySearchBar
//                    // Padding for the search bar within the custom top bar
//                    MySearchBar(
//                        modifier = Modifier.padding(horizontal = 16.dp),
//                        text = searchText,
//                        onTextChange = { newValue ->
//                            searchText = newValue
//                        },
//                        placeholder = "Search word...",
//                        onCloseClicked = {
//                            searchText = ""
//                        },
//                        onMicClicked = {
//                            // Handle mic click
//                        },
//                        onLanguageClicked = {
//
//                            println("Language icon clicked!")
//                        }
//                    )
//
//                    // Padding below the search bar
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                }
//            }
//        },
//        bottomBar = {
//            if (currentRoute in bottomNavRoutes) {
//                BottomNavigationUI(navController, pagerState)
//            }
//        }
//    ){ padding ->
//        Box(Modifier.padding(padding)) {
//            Navigation(navController = navController)
//        }
//    }
//}