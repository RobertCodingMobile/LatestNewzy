package com.robertcoding.paginationnews.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertcoding.domain.model.UserPreferences
import com.robertcoding.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val settingsState: StateFlow<UserPreferences> = userPreferencesRepository.userSettings
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserPreferences()
        )

    fun onDataSaverChanged(isDataSaverEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.toggleDataSaver(isDataSaverEnabled)
        }
    }

    fun onDarkModeChange(isDarkModeEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.toggleDarkMode(isDarkModeEnabled)
        }
    }
}