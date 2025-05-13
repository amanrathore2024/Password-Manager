package com.example.app.repo

import android.util.Log
import com.example.app.data.dao.PasswordItemDao
import com.example.app.data.entity.PasswordItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(
    private val passwordDao: PasswordItemDao
) {

    suspend fun upsertItem(item: PasswordItem) {
        val encrypted = item.copy(password = EncryptionUtils.encryptData(item.password))
        passwordDao.upsertItem(encrypted)
    }

    suspend fun deleteItem(item: PasswordItem) {
        passwordDao.deleteItem(item)
    }

    fun getPasswordItems(): Flow<List<PasswordItem>> {
        return passwordDao.getPasswordItems().map { list ->
            Log.d("Repository", "Fetched ${list.size} items from Room")
            list.map {
                try {
                    val decrypted = it.copy(password = EncryptionUtils.decryptData(it.password))
                    Log.d("Repository", "Decrypted password for item: ${it.id}")
                    decrypted
                } catch (e: Exception) {
                    Log.e("Repository", "Decryption failed for item ${it.id}: ${e.message}")
                    it.copy(password = "[Error]")
                }
            }
        }
    }
}





