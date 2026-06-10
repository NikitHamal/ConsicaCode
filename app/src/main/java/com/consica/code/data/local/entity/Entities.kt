package com.consica.code.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "total_xp")
    val totalXp: Int = 0,
    @ColumnInfo(name = "level")
    val level: Int = 1,
    @ColumnInfo(name = "sun_coins")
    val sunCoins: Int = 0,
    @ColumnInfo(name = "water_drops")
    val waterDrops: Int = 0,
    @ColumnInfo(name = "current_streak")
    val currentStreak: Int = 0,
    @ColumnInfo(name = "best_streak")
    val bestStreak: Int = 0,
    @ColumnInfo(name = "last_active_date")
    val lastActiveDate: Long = 0,
    @ColumnInfo(name = "total_lessons_completed")
    val totalLessonsCompleted: Int = 0,
    @ColumnInfo(name = "total_puzzles_completed")
    val totalPuzzlesCompleted: Int = 0,
    @ColumnInfo(name = "total_projects_completed")
    val totalProjectsCompleted: Int = 0,
    @ColumnInfo(name = "unlocked_biomes")
    val unlockedBiomes: String = "[]",
    @ColumnInfo(name = "ecosystem_items")
    val ecosystemItems: String = "[]",
    @ColumnInfo(name = "earned_badges")
    val earnedBadges: String = "[]",
    @ColumnInfo(name = "completed_lesson_ids")
    val completedLessonIds: String = "[]",
    @ColumnInfo(name = "completed_puzzle_ids")
    val completedPuzzleIds: String = "[]"
) {
    val xpForNextLevel: Int get() = level * 100 + 50
}

@Entity(tableName = "completed_lessons")
data class CompletedLessonEntity(
    @PrimaryKey
    val lessonId: String,
    @ColumnInfo(name = "completed_at")
    val completedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "attempts")
    val attempts: Int = 1,
    @ColumnInfo(name = "code_submitted")
    val codeSubmitted: String = "",
    @ColumnInfo(name = "time_spent_seconds")
    val timeSpentSeconds: Int = 0
)

@Entity(tableName = "workspaces")
data class WorkspaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "code_content")
    val codeContent: String = "",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_template")
    val isTemplate: Boolean = false,
    @ColumnInfo(name = "lesson_id")
    val lessonId: String? = null
)

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey
    val badgeId: String,
    @ColumnInfo(name = "earned_at")
    val earnedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "seen")
    val seen: Boolean = false
)

@Entity(tableName = "code_history")
data class CodeHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "lesson_id")
    val lessonId: String? = null,
    @ColumnInfo(name = "workspace_id")
    val workspaceId: Long? = null,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "code_content")
    val codeContent: String,
    @ColumnInfo(name = "output")
    val output: String = "",
    @ColumnInfo(name = "is_success")
    val isSuccess: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
