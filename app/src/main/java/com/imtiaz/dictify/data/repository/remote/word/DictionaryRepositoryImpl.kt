package com.imtiaz.dictify.data.repository.remote.word


import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.domain.repository.dictionary.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
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
            val errorToEmit = if (e is HttpException) {
                Exception("No Definitions Found! We couldn't find definitions for the word you were looking for")
            }
            else if (e is IOException) {
                Exception("Network problem detected. Check your connection.")
            }
            else {
                Exception("Something went wrong. Please try again.")
            }
            emit(DataState.Error(errorToEmit))

        }
    }

}