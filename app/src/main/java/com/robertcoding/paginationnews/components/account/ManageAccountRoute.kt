package com.robertcoding.paginationnews.components.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robertcoding.paginationnews.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageAccountRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {

    val settings by viewModel.settingsState.collectAsStateWithLifecycle()

    ManageAccountScreen(
        modifier = modifier,
        settings = settings,
        onDarkModeChange = viewModel::onDarkModeChange,
        onDataSaverChange = viewModel::onDataSaverChanged
    )

}