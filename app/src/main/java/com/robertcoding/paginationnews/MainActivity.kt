package com.robertcoding.paginationnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.robertcoding.paginationnews.navigation.NewsNavigation
import com.robertcoding.paginationnews.ui.theme.PaginationNewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaginationNewsTheme {
                Scaffold { innerPadding ->
                    NewsNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

