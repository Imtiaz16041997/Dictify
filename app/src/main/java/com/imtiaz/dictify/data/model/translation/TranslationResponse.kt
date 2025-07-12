package com.imtiaz.dictify.data.model.translation


import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    val translations: List<TranslationItem>
)

data class TranslationItem(
    @SerializedName("detected_source_language")
    val detectedSourceLanguage: String,
    val text: String
)