package com.imtiaz.dictify.presentation.screen.mainscreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.imtiaz.dictify.navigation.component.BottomNavigationUI
import com.imtiaz.dictify.navigation.Screen
import com.imtiaz.dictify.navigation.currentRoute
import androidx.hilt.navigation.compose.hiltViewModel
import com.imtiaz.dictify.navigation.AppNavGraph
import com.imtiaz.dictify.navigation.navigationTitle
import com.imtiaz.dictify.presentation.component.common.BaseTopBar
import com.imtiaz.dictify.presentation.component.dictionary.HomeTopBar
import com.imtiaz.dictify.presentation.screen.mainscreen.dictionaryviewmodel.MainViewModel
import java.util.Locale


@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)
    //val context = LocalContext.current
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
    //val focusManager = LocalFocusManager.current
    val mainViewModel: MainViewModel = hiltViewModel()
    //var searchText by remember { mutableStateOf("") }
   /* val searchText by mainViewModel.searchQuery.collectAsState()

    // 1. Setup ActivityResultLauncher for Speech Recognition
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val spokenTexts = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenTexts?.get(0) ?: ""

            if (spokenText.isNotBlank()) {
                // ONLY call triggerWordLookup.
                // triggerWordLookup already updates _searchQuery and emits to _explicitSearchTrigger.
                mainViewModel.triggerWordLookup(spokenText)
                focusManager.clearFocus() // Hide keyboard after voice input
            } else {
                Toast.makeText(context, "No speech recognized.", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(context, "Speech recognition cancelled.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Speech recognition error: ${result.resultCode}", Toast.LENGTH_SHORT).show()
        }
    }*/
    RequestNotificationPermission()
    Scaffold(
        topBar = {
            if (showTopAppBarActions) {
                // Dynamic Top Bar based on current route
                when (currentRoute) {
                    Screen.Home.route -> {
                        val screenTitle = navigationTitle(navController)
                        BaseTopBar(title = screenTitle)
                        /*HomeTopBar(
                            searchText = searchText,
                            onTextChange = { newValue -> mainViewModel.updateSearchQuery(newValue) },
                            onCloseClicked = { mainViewModel.updateSearchQuery("") },
                            onMicClicked = {
                                println("Mic icon clicked!")
                                launchSpeechRecognizer(speechRecognizerLauncher, context)
                                Toast.makeText(context, "Speak now...", Toast.LENGTH_SHORT).show()
                            },
                            onLanguageClicked = { println("Language icon clicked!") },
                            onSearchTriggered = { wordToSearch ->
                                if (wordToSearch.isNotBlank()) {
                                    mainViewModel.triggerWordLookup(wordToSearch)
                                    focusManager.clearFocus()
                                } else {
                                    mainViewModel.triggerWordLookup(wordToSearch)
                                    focusManager.clearFocus()
                                }
                            }
                        )*/
                    }
                    Screen.Bookmarks.route,
                    Screen.Translator.route,
                    Screen.Profile.route -> {
                        // Get the title dynamically based on the current route
                        val screenTitle = navigationTitle(navController)
                        BaseTopBar(title = screenTitle)
                    }
                    else -> {

                    }
                }
            }
        },
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationUI(navController, pagerState)
            }
        }
    )


    { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            AppNavGraph(navController = navController, mainViewModel = mainViewModel)
        }
    }
}




@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted! Notifications can now be shown.
            Toast.makeText(context, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            // Permission denied. You might want to explain why notifications are useful.
            Toast.makeText(context, "Notification permission denied. You won't receive word insights.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API level 33)
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the permission
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}