package com.shadow.ultrastudio.ui.generate

import com.shadow.ultrastudio.data.remote.dto.ModelInfoDto
import com.shadow.ultrastudio.data.remote.dto.MODEL_KIND_VIDEO_I2V

data class GenerateUiState(
    val models: Map<String, ModelInfoDto> = emptyMap(),
    val isLoadingModels: Boolean = true,
    val modelsError: String? = null,

    val selectedModelKey: String = "",
    val prompt: String = "",
    val negative: String = "",
    val width: Int = 512,
    val height: Int = 512,
    val steps: Int = 20,
    val cfg: Float = 8.0f,
    val sampler: String = "euler",
    val scheduler: String = "normal",
    val length: Int = 121,
    val fps: Int = 24,
    val seed: Long = 0L,
    val randomizeSeed: Boolean = true,
    val batchCount: Int = 1,
    val folder: String = "",
    val advancedExpanded: Boolean = false,

    val pickedImageName: String? = null,
    val isUploadingImage: Boolean = false,
    val uploadedImageFilename: String = "",

    val generalError: String? = null,
) {
    val selectedModel: ModelInfoDto? get() = models[selectedModelKey]
    val needsImageUpload: Boolean get() = selectedModel?.kind == MODEL_KIND_VIDEO_I2V
    val isVideoModel: Boolean get() = selectedModel?.kind?.startsWith("video") == true

    val canGenerate: Boolean get() =
        selectedModel != null &&
            prompt.isNotBlank() &&
            !isUploadingImage &&
            (!needsImageUpload || uploadedImageFilename.isNotBlank())
}
