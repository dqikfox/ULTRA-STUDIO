package com.shadow.ultrastudio.data.remote.api

import com.shadow.ultrastudio.data.remote.dto.AdminAccountsResponseDto
import com.shadow.ultrastudio.data.remote.dto.MutationResultDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AdminApi {
    @GET("/admin/api/accounts")
    suspend fun getAccounts(): Response<AdminAccountsResponseDto>

    @FormUrlEncoded
    @POST("/admin/api/accounts/disable")
    suspend fun setDisabled(
        @Field("username") username: String,
        @Field("disabled") disabled: Boolean,
    ): Response<MutationResultDto>

    @FormUrlEncoded
    @POST("/admin/api/accounts/admin")
    suspend fun setAdmin(
        @Field("username") username: String,
        @Field("is_admin") isAdmin: Boolean,
    ): Response<MutationResultDto>

    @FormUrlEncoded
    @POST("/admin/api/accounts/delete")
    suspend fun deleteAccount(@Field("username") username: String): Response<MutationResultDto>

    @FormUrlEncoded
    @POST("/admin/api/accounts/reset_password")
    suspend fun resetPassword(
        @Field("username") username: String,
        @Field("new_password") newPassword: String,
    ): Response<MutationResultDto>
}
