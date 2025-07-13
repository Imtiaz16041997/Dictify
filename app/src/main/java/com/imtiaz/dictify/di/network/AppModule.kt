package com.imtiaz.dictify.di.network

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import com.imtiaz.dictify.HiltWorkManagerInitializer
import com.imtiaz.dictify.data.dataSource.local.LanguageDatabase
import com.imtiaz.dictify.data.dataSource.local.LanguageRoomDao
import com.imtiaz.dictify.data.local.dao.DictionaryDao
import com.imtiaz.dictify.di.dictionary.DictionaryRepositoryModule
import com.imtiaz.dictify.di.translator.TranslatorRepositoryModule
import com.imtiaz.dictify.di.tts.TextToSpeechModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        DictionaryRepositoryModule::class,
        TranslatorRepositoryModule::class,
        TextToSpeechModule::class
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
    fun provideLanguageDao(database: LanguageDatabase): LanguageRoomDao {
        return database.languageDao()
    }

    @Provides
    @Singleton
    fun provideDictionaryDao(database: LanguageDatabase): DictionaryDao {
        return database.dictionaryDao()
    }

    @Provides
    @Singleton
    fun provideHiltWorkManagerInitializer(workerFactory: HiltWorkerFactory): HiltWorkManagerInitializer {
        return HiltWorkManagerInitializer(workerFactory)
    }


}