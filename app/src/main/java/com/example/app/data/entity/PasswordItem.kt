package com.example.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PasswordItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val Acctype: String,
    val username: String,
    val password: String
)
