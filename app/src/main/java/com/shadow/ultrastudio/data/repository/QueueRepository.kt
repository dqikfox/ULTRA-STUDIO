package com.shadow.ultrastudio.data.repository

import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.remote.api.QueueApi
import com.shadow.ultrastudio.data.remote.dto.QueueItemDto
import com.shadow.ultrastudio.data.remote.map
import com.shadow.ultrastudio.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueRepository @Inject constructor(
    private val queueApi: QueueApi,
) {
    suspend fun fetchQueue(): ApiResult<List<QueueItemDto>> =
        safeApiCall { queueApi.getQueue() }.map { it.items }
}
