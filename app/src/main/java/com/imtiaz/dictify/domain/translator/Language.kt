package com.imtiaz.dictify.domain.translator

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.imtiaz.dictify.R


@Entity(tableName = "languages")
data class Language(
    @PrimaryKey
    @SerializedName("language")
    val language: String,

    @SerializedName("name")
    val name: String,

    val flagResId: Int = R.drawable.ic_flag_ussss
)