package com.shadow.ultrastudio.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueueItemDto(
    val id: String,
    val kind: String,
    val mine: Boolean,
    val username: String,
    val model: String? = null, // redacted (null) server-side for jobs that aren't yours
    @SerialName("eta_seconds") val etaSeconds: Int,
    @SerialName("elapsed_seconds") val elapsedSeconds: Int,
)

@Serializable
data class QueueResponseDto(val items: List<QueueItemDto>)
