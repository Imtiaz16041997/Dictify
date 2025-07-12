package com.imtiaz.dictify.domain.translator

import com.imtiaz.dictify.domain.common.DataState
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
   /*Language*/


   suspend fun getLanguages(): Flow<DataState<List<Language>>>
   /*Translation*/
   suspend fun translateText(text: String, sourceLang: String, targetLang: String): Flow<DataState<String>>
}