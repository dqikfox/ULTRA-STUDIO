package com.shadow.ultrastudio.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadow.ultrastudio.data.remote.dto.AdminAccountDto
import com.shadow.ultrastudio.ui.common.ErrorBanner
import com.shadow.ultrastudio.ui.common.FullScreenLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onBack: () -> Unit, viewModel: AdminViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var deleteTarget by remember { mutableStateOf<String?>(null) }
    var resetTarget by remember { mutableStateOf<String?>(null) }
    var resetValue by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            state.error?.let { ErrorBanner(it) }

            if (state.isLoading) {
                FullScreenLoading(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn {
                    items(state.accounts, key = { it.username }) { account ->
                        AccountRow(
                            account = account,
                            isSelf = account.username == state.me,
                            onToggleAdmin = { viewModel.toggleAdmin(account.username, it) },
                            onToggleDisabled = { viewModel.toggleDisabled(account.username, it) },
                            onDelete = { deleteTarget = account.username },
                            onResetPassword = { resetTarget = account.username; resetValue = "" },
                        )
                    }
                }
            }
        }
    }

    deleteTarget?.let { username ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Delete account?") },
            text = { Text("\"$username\"'s media is archived, not permanently erased, but they'll lose access immediately.") },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteAccount(username); deleteTarget = null }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { deleteTarget = null }) { Text("Cancel") } },
        )
    }

    resetTarget?.let { username ->
        AlertDialog(
            onDismissRequest = { resetTarget = null },
            title = { Text("Reset password for \"$username\"") },
            text = {
                OutlinedTextField(
                    value = resetValue,
                    onValueChange = { resetValue = it },
                    label = { Text("New password (8+ characters)") },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.resetPassword(username, resetValue); resetTarget = null },
                    enabled = resetValue.length >= 8,
                ) { Text("Reset") }
            },
            dismissButton = { TextButton(onClick = { resetTarget = null }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun AccountRow(
    account: AdminAccountDto,
    isSelf: Boolean,
    onToggleAdmin: (Boolean) -> Unit,
    onToggleDisabled: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onResetPassword: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(account.username, style = MaterialTheme.typography.titleMedium)
            if (isSelf) Text("(you)", style = MaterialTheme.typography.labelSmall)
            if (account.disabled) Text("(disabled)", style = MaterialTheme.typography.labelSmall)
        }
        Text(
            "${account.images} images - ${account.videos} videos - created ${account.createdAt}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = account.isAdmin, onCheckedChange = onToggleAdmin, enabled = !isSelf)
            Text("Admin")
            Switch(checked = !account.disabled, onCheckedChange = { onToggleDisabled(!it) }, enabled = !isSelf)
            Text(if (account.disabled) "Disabled" else "Enabled")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(onClick = onResetPassword) { Text("Reset password") }
            TextButton(onClick = onDelete, enabled = !isSelf) { Text("Delete") }
        }
    }
}
