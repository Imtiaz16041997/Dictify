package com.imtiaz.dictify.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtiaz.dictify.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int = R.string.app_name,
    val navIcon: (@Composable () -> Unit) = {
        Icon(
            Icons.Filled.Home, contentDescription = "home"
        )
    },
    val objectName: String = "",
    val objectPath: String = ""
) {
    //bottom screen
    data object  Home : Screen ("home_page")
    data object  Bookmarks : Screen ("book_mark_page")
    data object  Translator : Screen ("translator_page")
    data object  Profile : Screen ("profile_page")


    //Bottom Navigation

    data object HomeNav : Screen("home_page", title = R.string.home, navIcon = {
        Icon(
            Icons.Filled.Home,
            contentDescription = "search",
            modifier = Modifier
                .padding(end = 16.dp)
                .offset(x = 10.dp)
        )
    })

    data object BookmarksNav : Screen("book_mark_page", title = R.string.book_mark, navIcon = {
        Icon(
            Icons.Filled.Bookmarks,
            contentDescription = "search",
            modifier = Modifier
                .padding(end = 16.dp)
                .offset(x = 10.dp)
        )
    })

    data object TranslatorNav : Screen("translator_page", title = R.string.translator, navIcon = {
        Icon(
            Icons.Filled.Translate,
            contentDescription = "search",
            modifier = Modifier
                .padding(end = 16.dp)
                .offset(x = 10.dp)
        )
    })

    data object ProfileNav : Screen("profile_page", title = R.string.profile, navIcon = {
        Icon(
            Icons.Filled.AccountBox,
            contentDescription = "search",
            modifier = Modifier
                .padding(end = 16.dp)
                .offset(x = 10.dp)
        )
    })

    data object WordDetail : Screen("word_detail", objectName = "wordItem", objectPath = "/{wordItem}")




}