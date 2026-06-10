package com.consica.code.data.local.dao

import androidx.room.*
import com.consica.code.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun observeProgress(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getProgress(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET total_xp = total_xp + :amount WHERE id = 1")
    suspend fun addXp(amount: Int)

    @Query("UPDATE user_progress SET sun_coins = sun_coins + :amount WHERE id = 1")
    suspend fun addSunCoins(amount: Int)

    @Query("UPDATE user_progress SET water_drops = water_drops + :amount WHERE id = 1")
    suspend fun addWaterDrops(amount: Int)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM completed_lessons")
    fun observeCompletedLessons(): Flow<List<CompletedLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedLesson(lesson: CompletedLessonEntity)

    @Query("SELECT * FROM completed_lessons WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getCompletedLesson(lessonId: String): CompletedLessonEntity?

    @Query("SELECT COUNT(*) FROM completed_lessons")
    suspend fun getCompletedCount(): Int
}

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM workspaces ORDER BY updated_at DESC")
    fun observeWorkspaces(): Flow<List<WorkspaceEntity>>

    @Query("SELECT * FROM workspaces WHERE id = :id")
    suspend fun getWorkspace(id: Long): WorkspaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorkspace(workspace: WorkspaceEntity): Long

    @Delete
    suspend fun deleteWorkspace(workspace: WorkspaceEntity)

    @Query("DELETE FROM workspaces WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    fun observeBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Query("SELECT * FROM badges WHERE badgeId = :badgeId LIMIT 1")
    suspend fun getBadge(badgeId: String): BadgeEntity?

    @Query("UPDATE badges SET seen = 1 WHERE badgeId = :badgeId")
    suspend fun markSeen(badgeId: String)
}

@Dao
interface CodeHistoryDao {
    @Query("SELECT * FROM code_history ORDER BY created_at DESC LIMIT :limit")
    fun observeRecentHistory(limit: Int = 50): Flow<List<CodeHistoryEntity>>

    @Insert
    suspend fun insertHistory(entry: CodeHistoryEntity): Long

    @Query("DELETE FROM code_history")
    suspend fun clearHistory()
}
