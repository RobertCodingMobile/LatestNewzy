package com.robertcoding.domain.repository

import com.robertcoding.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userSettings: Flow<UserPreferences>
    suspend fun toggleDataSaver(enabled: Boolean)

    suspend fun toggleDarkMode(enabled: Boolean)
}