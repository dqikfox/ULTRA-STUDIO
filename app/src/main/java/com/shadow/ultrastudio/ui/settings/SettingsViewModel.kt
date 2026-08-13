package com.shadow.ultrastudio.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.repository.AdminRepository
import com.shadow.ultrastudio.data.repository.AuthRepository
import com.shadow.ultrastudio.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val serverBaseUrl: String = "",
    val darkThemeOverride: Boolean? = null,
    val isAdmin: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val adminRepository: AdminRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.serverBaseUrl,
                settingsRepository.darkThemeOverride,
            ) { url, darkOverride -> url to darkOverride }
                .collect { (url, darkOverride) ->
                    _uiState.value = _uiState.value.copy(serverBaseUrl = url, darkThemeOverride = darkOverride)
                }
        }
        viewModelScope.launch {
            // No dedicated "am I admin" endpoint - this doubles as the check: success = admin.
            val isAdmin = adminRepository.fetchAccounts() is ApiResult.Success
            _uiState.value = _uiState.value.copy(isAdmin = isAdmin)
        }
    }

    fun setDarkThemeOverride(value: Boolean?) {
        viewModelScope.launch { settingsRepository.setDarkThemeOverride(value) }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
