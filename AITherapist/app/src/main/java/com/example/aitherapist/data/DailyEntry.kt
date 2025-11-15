package com.example.aitherapist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "daily_entries")
data class DailyEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val whatDid: String,
    val whatDidNot: String,
    val feelings: String,
    val createdAt: Long = System.currentTimeMillis()
)

