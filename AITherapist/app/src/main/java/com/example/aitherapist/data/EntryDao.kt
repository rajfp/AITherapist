package com.example.aitherapist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert
    suspend fun insertEntry(entry: DailyEntry): Long
    
    @Query("SELECT * FROM daily_entries ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestEntry(): DailyEntry?
    
    @Query("SELECT * FROM daily_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<DailyEntry>>
    
    @Query("SELECT * FROM daily_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryByDate(date: Long): DailyEntry?
}

