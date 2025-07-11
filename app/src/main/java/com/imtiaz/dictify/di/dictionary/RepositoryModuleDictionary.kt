package com.imtiaz.dictify.di.dictionary

import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.repository.remote.word.DictionaryRepositoryImpl
import com.imtiaz.dictify.di.qualifiers.DictionaryApi
import com.imtiaz.dictify.domain.repository.dictionary.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleDictionary {
    /**
     * Provides DictionaryRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideDictionaryRepository(
        @DictionaryApi apiService: ApiService, // <--- ADD QUALIFIER HERE
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            apiService
        )
    }
}