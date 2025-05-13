package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.app.data.entity.PasswordItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordItemDao {

    @Upsert
    suspend fun upsertItem( item: PasswordItem)

    @Delete
    suspend fun deleteItem(item: PasswordItem)

    @Query("SELECT * FROM PasswordItem ORDER BY Acctype ASC")
    fun getPasswordItems(): Flow<List<PasswordItem>>

}