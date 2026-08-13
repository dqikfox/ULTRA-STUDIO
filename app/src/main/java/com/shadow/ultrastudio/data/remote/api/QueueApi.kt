package com.shadow.ultrastudio.data.remote.api

import com.shadow.ultrastudio.data.remote.dto.QueueResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface QueueApi {
    @GET("/queue")
    suspend fun getQueue(): Response<QueueResponseDto>
}
