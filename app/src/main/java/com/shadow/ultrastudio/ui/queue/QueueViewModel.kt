package com.shadow.ultrastudio.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.remote.dto.QueueItemDto
import com.shadow.ultrastudio.data.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QueueUiState(
    val items: List<QueueItemDto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val queueRepository: QueueRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QueueUiState())
    val uiState: StateFlow<QueueUiState> = _uiState

    /** Called repeatedly by a LaunchedEffect tied to the screen's own composition,
     * so polling naturally pauses when the Queue tab isn't visible. */
    fun refreshOnce() {
        viewModelScope.launch {
            when (val result = queueRepository.fetchQueue()) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(
                    items = result.data,
                    isLoading = false,
                    error = null,
                )
                else -> _uiState.value = _uiState.value.copy(isLoading = false, error = "Couldn't refresh the queue.")
            }
        }
    }
}
