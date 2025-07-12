package com.imtiaz.dictify.data.model.translation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.imtiaz.dictify.R


@Entity(tableName = "languages")
data class Language(
    @PrimaryKey
    @SerializedName("language") // Used for JSON serialization/deserialization by Room/Gson
    val language: String, // This is the language code (e.g., "en", "es")

    @SerializedName("name") // Used for JSON serialization/deserialization by Room/Gson
    val name: String,

    val flagResId: Int = R.drawable.ic_flag_ussss // <--- Add a default value pointing to an existing drawable
)