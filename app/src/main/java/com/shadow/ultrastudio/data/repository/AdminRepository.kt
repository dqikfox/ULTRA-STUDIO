package com.shadow.ultrastudio.data.repository

import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.remote.api.AdminApi
import com.shadow.ultrastudio.data.remote.dto.AdminAccountsResponseDto
import com.shadow.ultrastudio.data.remote.dto.MutationResultDto
import com.shadow.ultrastudio.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val adminApi: AdminApi,
) {
    /** Also doubles as the "am I admin" check - there's no dedicated endpoint for that;
     * success means yes, [ApiResult.Forbidden] means no. */
    suspend fun fetchAccounts(): ApiResult<AdminAccountsResponseDto> = safeApiCall { adminApi.getAccounts() }

    suspend fun setDisabled(username: String, disabled: Boolean): ApiResult<MutationResultDto> =
        safeApiCall { adminApi.setDisabled(username, disabled) }

    suspend fun setAdmin(username: String, isAdmin: Boolean): ApiResult<MutationResultDto> =
        safeApiCall { adminApi.setAdmin(username, isAdmin) }

    suspend fun deleteAccount(username: String): ApiResult<MutationResultDto> =
        safeApiCall { adminApi.deleteAccount(username) }

    suspend fun resetPassword(username: String, newPassword: String): ApiResult<MutationResultDto> =
        safeApiCall { adminApi.resetPassword(username, newPassword) }
}
