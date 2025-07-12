package com.imtiaz.dictify.domain.dictionary

import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordDefinition(word: String, ): Flow<DataState<List<WordResponse>>>
}