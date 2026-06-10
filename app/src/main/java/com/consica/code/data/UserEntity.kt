package com.consica.code.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ageGroup: String, // "8-12", "13-16", "16+"
    val xp: Int = 0,
    val sunCoins: Int = 0,
    val waterDrops: Int = 0,
    val streak: Int = 0
)
