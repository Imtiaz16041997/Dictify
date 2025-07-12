package com.imtiaz.dictify.domain.translator


import com.google.gson.annotations.SerializedName

data class TranslationRequest(
    val text: List<String>,
    @SerializedName("target_lang")
    val targetLang: String
)