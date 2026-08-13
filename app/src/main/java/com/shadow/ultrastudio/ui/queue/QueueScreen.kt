package com.shadow.ultrastudio.ui.queue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadow.ultrastudio.data.remote.dto.QueueItemDto
import com.shadow.ultrastudio.ui.common.ErrorBanner
import com.shadow.ultrastudio.ui.common.FullScreenLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val POLL_INTERVAL_MS = 2500L

@Composable
fun QueueScreen(viewModel: QueueViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        while (isActive) {
            viewModel.refreshOnce()
            delay(POLL_INTERVAL_MS)
        }
    }

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                "Everyone's in-progress generations - other people's model/prompt stay hidden.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            state.error?.let { ErrorBanner(it) }

            if (state.isLoading) {
                FullScreenLoading(modifier = Modifier.fillMaxSize())
            } else if (state.items.isEmpty()) {
                Text("Nothing generating right now.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.items, key = { it.id }) { item -> QueueRow(item) }
                }
            }
        }
    }
}

@Composable
private fun QueueRow(item: QueueItemDto) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
            .padding(12.dp),
    ) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    item.username,
                    fontWeight = if (item.mine) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (item.mine) Text("(you)", style = MaterialTheme.typography.labelSmall)
            }
            Text(
                item.model?.let { "${item.kind} - $it" } ?: item.kind,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                "~${item.etaSeconds}s left (${item.elapsedSeconds}s elapsed)",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
