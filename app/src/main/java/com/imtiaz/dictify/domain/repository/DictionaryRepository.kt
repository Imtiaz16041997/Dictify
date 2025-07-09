package com.imtiaz.dictify.domain.repository

import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.model.WordsInformation
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordDefinition(
        word: String,
        targetLang: String = "en", // Default to English
        sourceLang: String = "en"  // Default to English
    ): Flow<DataState<WordsInformation>>
}