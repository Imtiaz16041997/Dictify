package com.imtiaz.dictify.data.model.translation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "languages")
data class Language(
    @PrimaryKey
    @SerializedName("language")
    val language: String,

    @SerializedName("name")
    val name: String
)