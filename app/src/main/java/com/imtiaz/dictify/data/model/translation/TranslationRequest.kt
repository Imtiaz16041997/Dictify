package com.imtiaz.dictify.data.model.translation


import com.google.gson.annotations.SerializedName

data class TranslationRequest(
    val text: List<String>, // API expects an array of strings
    @SerializedName("target_lang")
    val targetLang: String
)