package com.imtiaz.dictify.data.repository.remote.translator


import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.domain.repository.translator.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.imtiaz.dictify.data.model.translation.Language
import com.imtiaz.dictify.data.model.translation.TranslationRequest


class LanguageRepositoryImpl @Inject constructor(
    private val languageDao: LanguageDao,
    private val deeplApi: DeeplApiService
) : LanguageRepository {

    override suspend fun getLanguages(): Flow<DataState<List<Language>>> = flow {
        emit(DataState.Loading) // Start with loading state

        val cachedLanguages = languageDao.getAllLanguages()

        if (cachedLanguages.isNotEmpty()) {
            // If data exists in DB, emit it immediately as Success
            emit(DataState.Success(cachedLanguages))
        } else {
            // If DB is empty, fetch from API
            try {
                // Fetch List<LanguageApiDto> from API
                val languagesFromApiDto = deeplApi.getLanguages()
                // Convert DTOs to domain models, mapping flags
                val languagesFromApiDomain = languagesFromApiDto.map { it.toDomain() }

                // Store the fetched data in the database
                languageDao.insertAll(languagesFromApiDomain) // Ensure your DAO insertAll accepts List<Language>

                // Emit the newly stored data as Success
                emit(DataState.Success(languagesFromApiDomain))
            } catch (e: Exception) {
                // Emit an Error state if API call fails
                emit(DataState.Error(e))
            }
        }
    }

    override suspend fun translateText(
        text: String,
        sourceLang: String, // Note: Deepl API doesn't always need source_lang in request body, but for consistency we keep it.
        targetLang: String
    ): Flow<DataState<String>> = flow {
        emit(DataState.Loading) // Indicate loading state for translation
        try {
            // Create the request body according to the API's expectation
            val request = TranslationRequest(
                text = listOf(text), // API expects a list of texts
                targetLang = targetLang.uppercase() // Target language should be uppercase
            )

            // Call the translate API
            val response = deeplApi.translate(request)

            // Extract the translated text. Assuming there's at least one translation.
            val translatedText = response.translations.firstOrNull()?.text ?: ""

            emit(DataState.Success(translatedText)) // Emit the translated text as Success
        } catch (e: Exception) {
            emit(DataState.Error(e)) // Emit an Error state if translation fails
        }
    }
}