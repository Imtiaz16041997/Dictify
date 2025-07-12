package com.imtiaz.dictify.utils


import androidx.annotation.DrawableRes
import com.imtiaz.dictify.R

// Using an object for singleton access to the map
object FlagMapper {

    private val flagMap = mapOf(
        "AR" to R.drawable.ic_flag_us,
        "BG" to R.drawable.ic_flag_us,
        "CS" to R.drawable.ic_flag_us,
        "DA" to R.drawable.ic_flag_us,
        "DE" to R.drawable.ic_flag_us,
        "EL" to R.drawable.ic_flag_us,
        "EN" to R.drawable.ic_flag_us, // Example, ensure you have ic_flag_us
        "ES" to R.drawable.ic_flag_us, // Example, ensure you have ic_flag_es
        "ET" to R.drawable.ic_flag_us,
        "FI" to R.drawable.ic_flag_us,
        "FR" to R.drawable.ic_flag_us,
        "HU" to R.drawable.ic_flag_us,
        "ID" to R.drawable.ic_flag_us,
        "IT" to R.drawable.ic_flag_us,
        "JA" to R.drawable.ic_flag_us,
        "KO" to R.drawable.ic_flag_us,
        "LT" to R.drawable.ic_flag_us,
        "LV" to R.drawable.ic_flag_us,
        "NB" to R.drawable.ic_flag_us,
        "NL" to R.drawable.ic_flag_us,
        "PL" to R.drawable.ic_flag_us,
        "PT" to R.drawable.ic_flag_us,
        "RO" to R.drawable.ic_flag_us,
        "RU" to R.drawable.ic_flag_us,
        "SK" to R.drawable.ic_flag_us,
        "SL" to R.drawable.ic_flag_us,
        "SV" to R.drawable.ic_flag_us,
        "TR" to R.drawable.ic_flag_us,
        "UK" to R.drawable.ic_flag_us,
        "ZH" to R.drawable.ic_flag_us

    )

    @DrawableRes
    fun getFlagResIdForLanguageCode(code: String): Int {
        return flagMap.getOrDefault(code.uppercase(), R.drawable.ic_flag_us)
    }
}