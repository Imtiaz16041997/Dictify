package com.imtiaz.dictify.data.model.dictzilla

import com.google.gson.annotations.SerializedName

data class WordsInformation(
    @SerializedName("word")
    val word: String,

    @SerializedName("translation")
    val translation: String,

    @SerializedName("part_of_speech")
    val partsOfSpeech: String,

    @SerializedName("definition")
    val definition: String,

    @SerializedName("contextual_examples")
    val contextualExamples: List<ContextualExample>,

    @SerializedName("nuances")
    val nuances: List<String>,

    @SerializedName("plural")
    val plural: String?,

    @SerializedName("synonyms")
    val synonyms: List<String>,

    @SerializedName("antonyms")
    val antonyms: List<String>,

    @SerializedName("idioms")
    val idioms: List<String>,

    @SerializedName("proverbs")
    val proverbs: List<String>,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("register")
    val register: String,

    @SerializedName("collocations")
    val collocations: List<String>,

    @SerializedName("history")
    val history: String,

    @SerializedName("pronunciation")
    val pronunciation: String,

    @SerializedName("phrases")
    val phrases: List<String>,

    @SerializedName("dialogues")
    val dialogues: List<String>,

    @SerializedName("cultural_significance")
    val culturalSignificance: String
)

data class ContextualExample(
    @SerializedName("title")
    val title: String,

    @SerializedName("examples")
    val examples: List<String>,

    @SerializedName("translations")
    val translations: List<String>
)

