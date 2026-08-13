package com.shadow.ultrastudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadow.ultrastudio.data.repository.SettingsRepository
import com.shadow.ultrastudio.ui.navigation.UltraNavGraph
import com.shadow.ultrastudio.ui.theme.UltraStudioTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkOverride by settingsRepository.darkThemeOverride.collectAsStateWithLifecycle(initialValue = null)
            UltraStudioTheme(darkTheme = darkOverride ?: isSystemInDarkTheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UltraNavGraph()
                }
            }
        }
    }
}
