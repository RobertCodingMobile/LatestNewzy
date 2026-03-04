package com.robertcoding.domain.model

data class UserPreferences(
    val isDarkMode: Boolean = true,
    val useDynamicColor: Boolean = true,
    val isDataSaverEnabled: Boolean = false
)