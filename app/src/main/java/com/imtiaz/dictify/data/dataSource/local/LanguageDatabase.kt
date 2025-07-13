package com.imtiaz.dictify.data.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imtiaz.dictify.data.local.dao.DictionaryDao
import com.imtiaz.dictify.data.local.entity.DailyWordEntity
import com.imtiaz.dictify.data.local.entity.DictionaryWordEntity
import com.imtiaz.dictify.domain.translator.Language

@Database(entities = [Language::class, DictionaryWordEntity::class, DailyWordEntity::class], version = 1, exportSchema = false)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageRoomDao
    abstract fun dictionaryDao(): DictionaryDao
}