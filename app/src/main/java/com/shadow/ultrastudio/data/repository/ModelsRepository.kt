package com.shadow.ultrastudio.data.repository

import com.shadow.ultrastudio.data.remote.ApiResult
import com.shadow.ultrastudio.data.remote.api.GenerationApi
import com.shadow.ultrastudio.data.remote.dto.ModelInfoDto
import com.shadow.ultrastudio.data.remote.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelsRepository @Inject constructor(
    private val generationApi: GenerationApi,
) {
    suspend fun fetchModels(): ApiResult<Map<String, ModelInfoDto>> = safeApiCall { generationApi.getModels() }
}
