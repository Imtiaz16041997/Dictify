package com.imtiaz.dictify.data.repository.remote.dictionary

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.local.dao.DictionaryDao
import com.imtiaz.dictify.data.local.entity.DailyWordEntity
import com.imtiaz.dictify.data.local.entity.DictionaryWordEntity
import com.imtiaz.dictify.data.model.dictionary.WordResponse
import com.imtiaz.dictify.domain.common.DataState
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository
import com.imtiaz.dictify.domain.model.dictionary.FavoriteWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dictionaryDao: DictionaryDao
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
            } else if (e is IOException) {
                Exception("Network problem detected. Check your connection.")
            } else {
                Exception("Something went wrong. Please try again.")
            }
            emit(DataState.Error(errorToEmit))

        }
    }

    override suspend fun saveFavoriteWord(word: FavoriteWord) {
        dictionaryDao.insertFavoriteWord(word.toEntity())
    }

    override suspend fun deleteFavoriteWord(word: String) {
        dictionaryDao.deleteFavoriteWord(word)
    }

    override fun getAllFavoriteWords(): Flow<List<FavoriteWord>> {
        return dictionaryDao.getAllFavoriteWords().map { entities ->
            entities.map { it.toDomain() } // Map entities to domain models
        }
    }

    override fun isWordFavorite(word: String): Flow<Boolean> {
        return dictionaryDao.isWordFavorite(word)
    }

    override suspend fun insertDailyWord(dailyWord: DailyWordEntity) {
        dictionaryDao.insertDailyWord(dailyWord)
    }

    override fun getLatestDailyWord(): Flow<DailyWordEntity?> {
        return dictionaryDao.getLatestDailyWord()
    }

    override fun getAllDailyWords(): Flow<List<DailyWordEntity>> {
        return dictionaryDao.getAllDailyWords()
    }

}

// Mapper functions (can be in a separate Mappers.kt file or extensions)
fun DictionaryWordEntity.toDomain(): FavoriteWord {
    return FavoriteWord(
        word = word,
        definition = definition,
        partOfSpeech = partOfSpeech,
        audioUrl = audioUrl
    )
}

fun FavoriteWord.toEntity(): DictionaryWordEntity {
    return DictionaryWordEntity(
        word = word,
        definition = definition,
        partOfSpeech = partOfSpeech,
        audioUrl = audioUrl
    )
}