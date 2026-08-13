package com.shadow.ultrastudio.ui.nyx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.ultrastudio.data.repository.NyxRepository
import com.shadow.ultrastudio.data.repository.NyxTranscriptEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NyxUiState(
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val transcript: List<NyxTranscriptEntry> = emptyList(),
    val textInput: String = "",
    val error: String? = null,
)

@HiltViewModel
class NyxViewModel @Inject constructor(
    private val nyxRepository: NyxRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NyxUiState())
    val uiState: StateFlow<NyxUiState> = _uiState

    init {
        viewModelScope.launch {
            nyxRepository.transcript.collect { entries ->
                _uiState.value = _uiState.value.copy(transcript = entries)
            }
        }
        viewModelScope.launch {
            nyxRepository.connectionError.collect { error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(error = error, isConnecting = false, isConnected = false)
                }
            }
        }
    }

    fun startVoice() {
        _uiState.value = _uiState.value.copy(isConnecting = true, error = null)
        viewModelScope.launch {
            nyxRepository.startSession(NYX_AGENT_ID, textOnly = false)
            _uiState.value = _uiState.value.copy(isConnecting = false, isConnected = nyxRepository.isConnected)
        }
    }

    fun onTextInputChange(value: String) {
        _uiState.value = _uiState.value.copy(textInput = value)
    }

    fun sendText() {
        val text = _uiState.value.textInput.trim()
        if (text.isBlank()) return
        _uiState.value = _uiState.value.copy(textInput = "")
        viewModelScope.launch {
            if (!nyxRepository.isConnected) {
                _uiState.value = _uiState.value.copy(isConnecting = true, error = null)
                nyxRepository.startSession(NYX_AGENT_ID, textOnly = true)
                _uiState.value = _uiState.value.copy(isConnecting = false, isConnected = nyxRepository.isConnected)
            }
            nyxRepository.sendText(text)
        }
    }

    fun disconnect() {
        nyxRepository.endSession()
        _uiState.value = _uiState.value.copy(isConnected = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        nyxRepository.endSession()
        super.onCleared()
    }

    companion object {
        // The same Nyx agent already configured on ElevenLabs for the Ultra Studio web app.
        const val NYX_AGENT_ID = "agent_7501kyt0nj1rerx8xptpaee7mpy7"
    }
}
