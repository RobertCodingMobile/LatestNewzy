package com.robertcoding.paginationnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robertcoding.paginationnews.navigation.NewsNavigation
import com.robertcoding.paginationnews.settings.SettingsViewModel
import com.robertcoding.paginationnews.ui.theme.PaginationNewsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settings by viewModel.settingsState.collectAsStateWithLifecycle()

            PaginationNewsTheme(darkTheme = settings.isDarkMode) {
                Scaffold { innerPadding ->
                    NewsNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

