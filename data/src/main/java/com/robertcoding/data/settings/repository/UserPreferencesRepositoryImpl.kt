package com.robertcoding.data.settings.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.robertcoding.domain.model.UserPreferences
import com.robertcoding.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {
    override val userSettings: Flow<UserPreferences>
        get() = dataStore.data.map { prefs ->
            UserPreferences(
                isDarkMode = prefs[PreferencesKeys.DARK_MODE] ?: false,
                useDynamicColor = prefs[PreferencesKeys.DYNAMIC_COLOR] ?: true,
                isDataSaverEnabled = prefs[PreferencesKeys.DATA_SAVER] ?: false
            )
        }

    override suspend fun toggleDataSaver(enabled: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.DATA_SAVER] = enabled
        }
    }

    override suspend fun toggleDarkMode(enabled: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.DARK_MODE] = enabled
        }
    }
}

private object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val DATA_SAVER = booleanPreferencesKey("data_saver")
}