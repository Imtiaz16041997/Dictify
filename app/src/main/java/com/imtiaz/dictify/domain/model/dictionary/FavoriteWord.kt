package com.imtiaz.dictify.domain.model.dictionary

data class FavoriteWord(
    val word: String,
    val definition: String?,
    val partOfSpeech: String?,
    val audioUrl: String?
)