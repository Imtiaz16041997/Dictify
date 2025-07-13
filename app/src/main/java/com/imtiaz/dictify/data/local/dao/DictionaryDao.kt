package com.imtiaz.dictify.data.local.dao



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imtiaz.dictify.data.local.entity.DailyWordEntity
import com.imtiaz.dictify.data.local.entity.DictionaryWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteWord(word: DictionaryWordEntity)

    @Query("SELECT * FROM favorite_words ORDER BY word ASC") // Order alphabetically
    fun getAllFavoriteWords(): Flow<List<DictionaryWordEntity>>

    @Query("DELETE FROM favorite_words WHERE word = :word")
    suspend fun deleteFavoriteWord(word: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_words WHERE word = :word LIMIT 1)")
    fun isWordFavorite(word: String): Flow<Boolean>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWord(dailyWord: DailyWordEntity)

    @Query("SELECT * FROM daily_words ORDER BY fetchedDate DESC LIMIT 1")
    fun getLatestDailyWord(): Flow<DailyWordEntity?>

    @Query("SELECT * FROM daily_words ORDER BY fetchedDate DESC")
    fun getAllDailyWords(): Flow<List<DailyWordEntity>> // For displaying history


}