package com.imtiaz.dictify.data.model.translation



import com.google.gson.annotations.SerializedName
import com.imtiaz.dictify.domain.translator.Language
import com.imtiaz.dictify.utils.FlagMapper

data class LanguageApiDto(
    @SerializedName("language")
    val languageCode: String, // Use a distinct name like 'languageCode'
    val name: String
) {
    // Function to map DTO to your domain model, adding the flag
    fun toDomain(): Language {
        return Language(
            language = this.languageCode, // Maps API's language code to your domain's 'language' field
            name = this.name,
            flagResId = FlagMapper.getFlagResIdForLanguageCode(this.languageCode)
        )
    }
}



