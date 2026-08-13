package com.shadow.ultrastudio.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadow.ultrastudio.ui.common.GlassPanel

@Composable
fun SettingsScreen(
    onLoggedOut: () -> Unit,
    onEditServerUrl: () -> Unit,
    onOpenAdmin: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Server", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Text(state.serverBaseUrl.ifBlank { "Not set" }, style = MaterialTheme.typography.bodyMedium)
                    TextButton(onClick = onEditServerUrl) { Text("Change server address") }
                }
            }

            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Theme", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = state.darkThemeOverride == null,
                            onClick = { viewModel.setDarkThemeOverride(null) },
                            label = { Text("System") },
                        )
                        FilterChip(
                            selected = state.darkThemeOverride == false,
                            onClick = { viewModel.setDarkThemeOverride(false) },
                            label = { Text("Light") },
                        )
                        FilterChip(
                            selected = state.darkThemeOverride == true,
                            onClick = { viewModel.setDarkThemeOverride(true) },
                            label = { Text("Dark") },
                        )
                    }
                }
            }

            if (state.isAdmin) {
                TextButton(onClick = onOpenAdmin) { Text("Admin dashboard") }
            }

            Button(onClick = { viewModel.logout(onLoggedOut) }, modifier = Modifier.fillMaxWidth()) {
                Text("Log out")
            }
        }
    }
}
