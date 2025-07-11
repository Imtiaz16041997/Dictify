package com.imtiaz.dictify.di.translator

import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.data.repository.remote.translator.LanguageRepositoryImpl
import com.imtiaz.dictify.domain.repository.translator.LanguageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleTranslator {

    @Provides
    @Singleton
    fun provideLanguageRepository(
        languageDao: LanguageDao, // Unqualified, provided by AppModule
        deeplApi: DeeplApiService // <--- Now injects the UNQUALIFIED DeeplApiService
    ): LanguageRepository {
        return LanguageRepositoryImpl(languageDao, deeplApi)
    }
}