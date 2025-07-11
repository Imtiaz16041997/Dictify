package com.imtiaz.dictify.di

import android.content.Context
import androidx.room.Room
import com.imtiaz.dictify.data.dataSource.local.LanguageDatabase
import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.di.dictionary.NetworkModuleDictionary
import com.imtiaz.dictify.di.dictionary.RepositoryModuleDictionary
import com.imtiaz.dictify.di.translator.NetworkModuleTranslator
import com.imtiaz.dictify.di.translator.RepositoryModuleTranslator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        CommonNetworkModule::class, // <--- IMPORTANT: Include your common module here
        NetworkModuleDictionary::class,
        NetworkModuleTranslator::class,
        RepositoryModuleDictionary::class, // Assuming these provide the interfaces
        RepositoryModuleTranslator::class // Assuming these provide the interfaces
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    // These should be here, and ONLY here (not in RepositoryModuleTranslator)
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

    // Other app-wide dependencies
}