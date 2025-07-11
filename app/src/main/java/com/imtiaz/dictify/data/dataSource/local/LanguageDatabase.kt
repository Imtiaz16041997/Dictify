package com.imtiaz.dictify.data.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imtiaz.dictify.data.dataSource.local.dao.LanguageDao
import com.imtiaz.dictify.data.model.translation.Language

@Database(entities = [Language::class], version = 1, exportSchema = false)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}