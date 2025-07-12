package com.imtiaz.dictify.di.dictionary

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.repository.remote.word.DictionaryRepositoryImpl
import com.imtiaz.dictify.domain.repository.dictionary.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DictionaryRepositoryModule {
    @Singleton
    @Provides
    fun provideDictionaryRepository(
        apiService: ApiService, // <--- Now injects the UNQUALIFIED ApiService
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(apiService)
    }
}