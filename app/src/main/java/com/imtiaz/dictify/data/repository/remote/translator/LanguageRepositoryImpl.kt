package com.imtiaz.dictify.data.repository.remote.translator


import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.domain.repository.translator.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.imtiaz.dictify.data.model.translation.Language


class LanguageRepositoryImpl @Inject constructor(
    private val languageDao: LanguageDao,
    private val deeplApi: DeeplApiService
) : LanguageRepository {

    override suspend fun getLanguages(): Flow<List<Language>> = flow {
        // 1. Check if the database has languages.
        val cachedLanguages = languageDao.getAllLanguages()

        if (cachedLanguages.isNotEmpty()) {
            // 2. If data exists, emit it immediately from the database.
            emit(cachedLanguages)
            // No API call is made, fulfilling your requirement.
        } else {
            // 3. If the database is empty, fetch from the API.
            try {
                val languagesFromApi = deeplApi.getLanguages()
                // 4. Store the fetched data in the database.
                languageDao.insertAll(languagesFromApi)
                // 5. Emit the newly stored data from the database.
                emit(languagesFromApi)
            } catch (e: Exception) {
                // Handle network errors gracefully
                // You could emit an empty list or an error state here
                emit(emptyList())
            }
        }
    }


}