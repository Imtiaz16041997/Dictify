package com.imtiaz.dictify.domain.repository.translator

import com.imtiaz.dictify.data.model.translation.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
   suspend fun getLanguages(): Flow<List<Language>>
}