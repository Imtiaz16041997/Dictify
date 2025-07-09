package com.imtiaz.dictify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.imtiaz.dictify.presentation.screen.mainscreen.MainScreen
import com.imtiaz.dictify.presentation.theme.DictifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {
            DictifyTheme {
                MainScreen()
            }
        }
    }
}

