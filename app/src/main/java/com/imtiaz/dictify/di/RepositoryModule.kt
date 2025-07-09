package com.imtiaz.dictify.di

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.repository.remote.word.DictionaryRepositoryImpl
import com.imtiaz.dictify.domain.repository.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides DictionaryRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideDictionaryRepository(
        apiService: ApiService,
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            apiService
        )
    }

}