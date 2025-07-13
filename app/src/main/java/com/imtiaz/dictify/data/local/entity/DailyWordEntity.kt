package com.imtiaz.dictify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_words")
data class DailyWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Unique ID for each entry
    val word: String,
    val definition: String?,
    val partOfSpeech: String?,
    val audioUrl: String?,
    val fetchedDate: Long // Timestamp when the word was fetched (e.g., System.currentTimeMillis())
)