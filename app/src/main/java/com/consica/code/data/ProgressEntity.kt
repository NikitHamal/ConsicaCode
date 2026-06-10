package com.consica.code.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val completed: Boolean = false,
    val unlocked: Boolean = false,
    val starsEarned: Int = 0
)
