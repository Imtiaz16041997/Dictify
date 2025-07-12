package com.imtiaz.dictify.domain.dictionary

import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.domain.model.dictionary.FavoriteWord
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordDefinition(word: String, ): Flow<DataState<List<WordResponse>>>
    // New methods for favorites
    suspend fun saveFavoriteWord(word: FavoriteWord)
    suspend fun deleteFavoriteWord(word: String)
    fun getAllFavoriteWords(): Flow<List<FavoriteWord>>
    fun isWordFavorite(word: String): Flow<Boolean>
}