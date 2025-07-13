package com.imtiaz.dictify.domain.translator


import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    val translations: List<TranslationItem>
)

data class TranslationItem(
    @SerializedName("detected_source_language")
    val detectedSourceLanguage: String,
    val text: String
)