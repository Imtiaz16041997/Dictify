package com.imtiaz.dictify.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_words")
data class DictionaryWordEntity(
    @PrimaryKey val word: String,
    val definition: String?,
    val partOfSpeech: String?, // You might want to save the first part of speech
    val audioUrl: String? // If you want to store the pronunciation audio
)