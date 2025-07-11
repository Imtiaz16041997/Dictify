package com.imtiaz.dictify.di.translator

import android.content.Context
import androidx.room.Room
import com.imtiaz.dictify.data.dataSource.local.LanguageDatabase
import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.data.dataSource.remote.DeeplApiService
import com.imtiaz.dictify.data.repository.remote.translator.LanguageRepositoryImpl
import com.imtiaz.dictify.di.qualifiers.TranslatorApi
import com.imtiaz.dictify.domain.repository.translator.LanguageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleTranslator {

    // IMPORTANT: REMOVE these methods if AppModule already provides them!
    // If you leave them here AND in AppModule, you WILL get DuplicateBindings.
    // Assuming AppModule handles this, these two methods should be GONE from here.
    // @Provides
    // @Singleton
    // fun provideLanguageDatabase(@ApplicationContext context: Context): LanguageDatabase {
    //     return Room.databaseBuilder(
    //         context,
    //         LanguageDatabase::class.java,
    //         "language_db"
    //     ).build()
    // }

    // @Provides
    // @Singleton
    // fun provideLanguageDao(database: LanguageDatabase): LanguageDao {
    //     return database.languageDao()
    // }

    @Provides
    @Singleton
    fun provideLanguageRepository(
        languageDao: LanguageDao,
        @TranslatorApi deeplApi: DeeplApiService // <--- ADD QUALIFIER HERE (Corrected from previous)
    ): LanguageRepository {
        return LanguageRepositoryImpl(languageDao, deeplApi)
    }
}

/*
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModuleTranslator {

    @Provides
    @Singleton
    fun provideLanguageDatabase(@ApplicationContext context: Context): LanguageDatabase {
        return Room.databaseBuilder(
            context,
            LanguageDatabase::class.java,
            "language_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLanguageDao(database: LanguageDatabase): LanguageDao {
        return database.languageDao()
    }



    @Provides
    @Singleton
    fun provideLanguageRepository(
        languageDao: LanguageDao,
        deeplApi: DeeplApiService
    ): LanguageRepository {
        return LanguageRepositoryImpl(languageDao, deeplApi)
    }
}*/
