package com.imtiaz.dictify.data.model.translation



import com.google.gson.annotations.SerializedName
import com.imtiaz.dictify.R

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
            flagResId = getFlagResIdForLanguageCode(this.languageCode) // This is where the flag ID is injected
        )
    }
}


fun getFlagResIdForLanguageCode(code: String): Int {
    return when (code.uppercase()) {
        "AR" -> R.drawable.ic_flag_ussss      // Arabic - Saudi Arabia
        "BG" -> R.drawable.ic_flag_ussss      // Bulgarian - Bulgaria
        "CS" -> R.drawable.ic_flag_ussss      // Czech - Czech Republic
        "DA" -> R.drawable.ic_flag_ussss      // Danish - Denmark
        "DE" -> R.drawable.ic_flag_ussss      // German - Germany
        "EL" -> R.drawable.ic_flag_ussss      // Greek - Greece
        "EN" -> R.drawable.ic_flag_ussss      // English - USA
        "ES" -> R.drawable.ic_flag_ess      // Spanish - Spain
        "ET" -> R.drawable.ic_flag_ess      // Estonian - Estonia
        "FI" -> R.drawable.ic_flag_ess      // Finnish - Finland
        "FR" -> R.drawable.ic_flag_ess      // French - France
        "HU" -> R.drawable.ic_flag_ess      // Hungarian - Hungary
        "ID" -> R.drawable.ic_flag_ess      // Indonesian - Indonesia
        "IT" -> R.drawable.ic_flag_ess      // Italian - Italy
        "JA" -> R.drawable.ic_flag_ussss      // Japanese - Japan
        "KO" -> R.drawable.ic_flag_ussss      // Korean - South Korea
        "LT" -> R.drawable.ic_flag_ussss      // Lithuanian - Lithuania
        "LV" -> R.drawable.ic_flag_ussss      // Latvian - Latvia
        "NB" -> R.drawable.ic_flag_ussss      // Norwegian - Norway
        "NL" -> R.drawable.ic_flag_ussss      // Dutch - Netherlands
        "PL" -> R.drawable.ic_flag_ussss      // Polish - Poland
        "PT" -> R.drawable.ic_flag_ussss      // Portuguese - Portugal
        "RO" -> R.drawable.ic_flag_ussss      // Romanian - Romania
        "RU" -> R.drawable.ic_flag_ussss      // Russian - Russia
        "SK" -> R.drawable.ic_flag_ess      // Slovak - Slovakia
        "SL" -> R.drawable.ic_flag_ess      // Slovenian - Slovenia
        "SV" -> R.drawable.ic_flag_ess      // Swedish - Sweden
        "TR" -> R.drawable.ic_flag_ess      // Turkish - Turkey
        "UK" -> R.drawable.ic_flag_ess      // Ukrainian - Ukraine
        "ZH" -> R.drawable.ic_flag_ess      // Chinese - China
        else -> R.drawable.ic_flag_ess // A default/transparent flag
    }
}
