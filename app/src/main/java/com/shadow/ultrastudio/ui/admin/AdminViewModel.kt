package com.shadow.ultrastudio.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.remote.dto.AdminAccountDto
import com.shadow.ultrastudio.data.remote.dto.MutationResultDto
import com.shadow.ultrastudio.data.remote.errorMessage
import com.shadow.ultrastudio.data.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val accounts: List<AdminAccountDto> = emptyList(),
    val me: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = adminRepository.fetchAccounts()) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(
                    accounts = result.data.accounts,
                    me = result.data.me,
                    isLoading = false,
                )
                else -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.errorMessage())
            }
        }
    }

    fun toggleDisabled(username: String, disabled: Boolean) {
        viewModelScope.launch { handleMutation(adminRepository.setDisabled(username, disabled)) }
    }

    fun toggleAdmin(username: String, isAdmin: Boolean) {
        viewModelScope.launch { handleMutation(adminRepository.setAdmin(username, isAdmin)) }
    }

    fun deleteAccount(username: String) {
        viewModelScope.launch { handleMutation(adminRepository.deleteAccount(username)) }
    }

    fun resetPassword(username: String, newPassword: String) {
        viewModelScope.launch { handleMutation(adminRepository.resetPassword(username, newPassword)) }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun handleMutation(result: ApiResult<MutationResultDto>) {
        when (result) {
            is ApiResult.Success -> {
                if (result.data.ok) refresh() else _uiState.value = _uiState.value.copy(error = result.data.error)
            }
            else -> _uiState.value = _uiState.value.copy(error = result.errorMessage())
        }
    }
}
