package com.example.pikboard.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val username: String,
    val email: String,
    val phone: String?,
    val image: String?,
)