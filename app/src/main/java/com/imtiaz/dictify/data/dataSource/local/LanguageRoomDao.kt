package com.imtiaz.dictify.data.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imtiaz.dictify.domain.translator.Language

@Dao
interface LanguageRoomDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(languages: List<Language>)

    @Query("SELECT * FROM languages")
    suspend fun getAllLanguages(): List<Language>

    @Query("SELECT COUNT(*) FROM languages")
    suspend fun getLanguageCount(): Int
}