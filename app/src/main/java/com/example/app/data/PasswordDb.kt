package com.example.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app.data.dao.PasswordItemDao
import com.example.app.data.entity.PasswordItem

@Database(
    entities = [PasswordItem::class],
    version = 1,
    exportSchema = true
)
abstract class PasswordDb(): RoomDatabase() {
    abstract fun passwordDao() : PasswordItemDao
}