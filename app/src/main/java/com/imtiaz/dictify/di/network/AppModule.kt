package com.imtiaz.dictify.di.network

import android.content.Context
import androidx.room.Room
import com.imtiaz.dictify.data.dataSource.local.LanguageDatabase
import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.di.dictionary.RepositoryModuleDictionary
import com.imtiaz.dictify.di.translator.RepositoryModuleTranslator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        RepositoryModuleDictionary::class,
        RepositoryModuleTranslator::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {

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


}