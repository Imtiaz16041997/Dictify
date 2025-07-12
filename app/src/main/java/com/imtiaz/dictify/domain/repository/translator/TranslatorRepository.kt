package com.imtiaz.dictify.domain.repository.translator

import com.imtiaz.dictify.data.common.DataState
import com.imtiaz.dictify.data.model.translation.Language
import com.imtiaz.dictify.data.model.translation.LanguageApiDto
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
   //suspend fun getLanguages(): Flow<List<Language>>
   // Returns Flow<DataState<String>> for translation result
   suspend fun getLanguages(): Flow<DataState<List<Language>>>

   // Returns Flow<DataState<String>> for translation result
   suspend fun translateText(text: String, sourceLang: String, targetLang: String): Flow<DataState<String>>
}