package com.example.aitherapist.data

import kotlinx.coroutines.flow.Flow

class EntryRepository(private val entryDao: EntryDao) {
    suspend fun insertEntry(entry: DailyEntry): Long {
        return entryDao.insertEntry(entry)
    }
    
    suspend fun getLatestEntry(): DailyEntry? {
        return entryDao.getLatestEntry()
    }
    
    fun getAllEntries(): Flow<List<DailyEntry>> {
        return entryDao.getAllEntries()
    }
    
    suspend fun getEntryByDate(date: Long): DailyEntry? {
        return entryDao.getEntryByDate(date)
    }
}

