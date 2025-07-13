package com.imtiaz.dictify.di.dictionary

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.local.dao.DictionaryDao
import com.imtiaz.dictify.data.repository.remote.dictionary.DictionaryRepositoryImpl
import com.imtiaz.dictify.domain.dictionary.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryRepositoryModule {

    @Provides
    @Singleton
    fun provideDictionaryRepository(
        apiService: ApiService,
        dictionaryDao: DictionaryDao // Provide the DAO here
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(apiService, dictionaryDao)
    }


}

/*
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
}*/
