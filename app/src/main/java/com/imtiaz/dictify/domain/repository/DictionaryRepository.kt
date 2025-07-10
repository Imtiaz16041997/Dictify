package com.imtiaz.dictify.domain.repository

import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.data.model.dictzilla.WordsInformation
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordDefinition(word: String, ): Flow<DataState<List<WordResponse>>>
}