package com.imtiaz.dictify.data.model

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
    @SerializedName("items")
    val items: List<Language>
)

data class Language(
    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String
)
