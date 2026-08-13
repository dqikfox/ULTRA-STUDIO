package com.shadow.ultrastudio.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminAccountDto(
    val username: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_admin") val isAdmin: Boolean,
    val disabled: Boolean,
    val images: Int,
    val videos: Int,
    val bytes: Long,
)

@Serializable
data class AdminAccountsResponseDto(val accounts: List<AdminAccountDto>, val me: String)
