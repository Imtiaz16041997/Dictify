package com.imtiaz.dictify.data.repository.remote.word


import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.data.model.dictzilla.WordsInformation
import com.imtiaz.dictify.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DictionaryRepository {

    override suspend fun getWordDefinition(
        word: String,

    ): Flow<DataState<List<WordResponse>>> = flow {
        emit(DataState.Loading)
        try {
            // Call the API service method
            val definition = apiService.findingWordTranslation(word)
            emit(DataState.Success(definition))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

}