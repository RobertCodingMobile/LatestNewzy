package com.robertcoding.paginationnews.components.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DataSaverOn
import androidx.compose.material.icons.outlined.DataSaverOff
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robertcoding.domain.model.UserPreferences

@Composable
fun ManageAccountScreen(
    modifier: Modifier = Modifier,
    settings: UserPreferences,
    onDarkModeChange: (Boolean) -> Unit,
    onDataSaverChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //TITlE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "App Settings", style = MaterialTheme.typography.titleLarge)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleLarge)

            DarkModeToggleButton(
                modifier = Modifier.size(50.dp),
                isDarkMode = settings.isDarkMode,
                onToggle = {
                    onDarkModeChange(it)
                })
        }


        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Data Usage", style = MaterialTheme.typography.titleLarge)

            DataSaverToggleButton(
                modifier = Modifier.size(50.dp),
                isDataSaverEnabled = settings.isDataSaverEnabled,
                onToggle = {
                    onDataSaverChange(it)
                }
            )
        }
    }
}

@Composable
fun DarkModeToggleButton(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconToggleButton(
        checked = isDarkMode,
        onCheckedChange = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Outlined.LightMode,
            contentDescription = if (isDarkMode) "Switch to light mode" else "Switch to dark mode"
        )
    }
}

@Composable
fun DataSaverToggleButton(
    isDataSaverEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconToggleButton(
        checked = isDataSaverEnabled,
        onCheckedChange = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDataSaverEnabled) Icons.Filled.DataSaverOn else Icons.Outlined.DataSaverOff,
            contentDescription = if (isDataSaverEnabled) "Disable data saver" else "Enable data saver"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ManageAccountScreenPreview() {
    ManageAccountScreen(
        settings = UserPreferences(),
        onDarkModeChange = {},
        onDataSaverChange = {}
    )
}