package com.consica.code.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.consica.code.data.local.AppDatabase
import com.consica.code.data.local.entity.*
import com.consica.code.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppRepository(
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>
) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val progressDao = database.progressDao()
    private val lessonDao = database.lessonDao()
    private val workspaceDao = database.workspaceDao()
    private val badgeDao = database.badgeDao()
    private val codeHistoryDao = database.codeHistoryDao()

    // --- Onboarding / User Profile ---

    companion object {
        val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val KEY_AGE_GROUP = stringPreferencesKey("age_group")
        val KEY_AGE = intPreferencesKey("user_age")
        val KEY_LEARNING_GOAL = stringPreferencesKey("learning_goal")
        val KEY_EXPERIENCE = stringPreferencesKey("experience_level")
        val KEY_THEME_INTENSITY = stringPreferencesKey("theme_intensity")
        val KEY_INTERESTS = stringPreferencesKey("interests")
        val KEY_PRO_MODE_UNLOCKED = booleanPreferencesKey("pro_mode_unlocked")
        val KEY_HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val KEY_REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val KEY_GUIDANCE_LEVEL = stringPreferencesKey("guidance_level")
    }

    val onboardingComplete: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETE] ?: false
    }

    val userProfile: Flow<UserProfile> = dataStore.data.map { prefs ->
        UserProfile(
            ageGroup = AgeGroup.valueOf(prefs[KEY_AGE_GROUP] ?: AgeGroup.YOUNG.name),
            age = prefs[KEY_AGE] ?: 10,
            learningGoal = LearningGoal.valueOf(prefs[KEY_LEARNING_GOAL] ?: LearningGoal.EXPLORE.name),
            experienceLevel = ExperienceLevel.valueOf(prefs[KEY_EXPERIENCE] ?: ExperienceLevel.BEGINNER.name),
            themeIntensity = ThemeIntensity.valueOf(prefs[KEY_THEME_INTENSITY] ?: ThemeIntensity.PLAYFUL.name),
            interests = (prefs[KEY_INTERESTS] ?: "").split(",").filter { it.isNotBlank() }.toSet(),
            onboardingComplete = prefs[KEY_ONBOARDING_COMPLETE] ?: false,
            professionalModeUnlocked = prefs[KEY_PRO_MODE_UNLOCKED] ?: false
        )
    }

    suspend fun completeOnboarding(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETE] = true
            prefs[KEY_AGE_GROUP] = profile.ageGroup.name
            prefs[KEY_AGE] = profile.age
            prefs[KEY_LEARNING_GOAL] = profile.learningGoal.name
            prefs[KEY_EXPERIENCE] = profile.experienceLevel.name
            prefs[KEY_THEME_INTENSITY] = profile.themeIntensity.name
            prefs[KEY_INTERESTS] = profile.interests.joinToString(",")
        }
        // Initialize progress
        progressDao.upsertProgress(UserProgressEntity())
    }

    val settingsFlow: Flow<Map<String, Any>> = dataStore.data.map { prefs ->
        mapOf(
            "high_contrast" to (prefs[KEY_HIGH_CONTRAST] ?: false),
            "reduced_motion" to (prefs[KEY_REDUCED_MOTION] ?: false),
            "sound_enabled" to (prefs[KEY_SOUND_ENABLED] ?: true),
            "guidance_level" to (prefs[KEY_GUIDANCE_LEVEL] ?: "full")
        )
    }

    suspend fun updateSetting(key: String, value: Any) {
        dataStore.edit { prefs ->
            when (key) {
                "high_contrast" -> prefs[KEY_HIGH_CONTRAST] = value as Boolean
                "reduced_motion" -> prefs[KEY_REDUCED_MOTION] = value as Boolean
                "sound_enabled" -> prefs[KEY_SOUND_ENABLED] = value as Boolean
                "guidance_level" -> prefs[KEY_GUIDANCE_LEVEL] = value as String
                "pro_mode_unlocked" -> prefs[KEY_PRO_MODE_UNLOCKED] = value as Boolean
            }
        }
    }

    suspend fun updateProfile(field: String, value: String) {
        dataStore.edit { prefs ->
            when (field) {
                "age_group" -> prefs[KEY_AGE_GROUP] = value
                "theme_intensity" -> prefs[KEY_THEME_INTENSITY] = value
                "learning_goal" -> prefs[KEY_LEARNING_GOAL] = value
                "experience_level" -> prefs[KEY_EXPERIENCE] = value
            }
        }
    }

    // --- Progress ---

    val userProgress: Flow<UserProgressEntity?> = progressDao.observeProgress()

    suspend fun getProgressSnapshot(): UserProgressEntity? = progressDao.getProgress()

    suspend fun awardXp(amount: Int) {
        progressDao.addXp(amount)
        val progress = progressDao.getProgress() ?: return
        val newLevel = progress.totalXp / progress.xpForNextLevel + 1
        if (newLevel > progress.level) {
            progressDao.upsertProgress(progress.copy(level = newLevel))
        }
    }

    suspend fun awardSunCoins(amount: Int) = progressDao.addSunCoins(amount)
    suspend fun awardWaterDrops(amount: Int) = progressDao.addWaterDrops(amount)

    suspend fun completeLesson(lesson: Lesson, code: String, timeSpent: Int): Boolean {
        val existing = lessonDao.getCompletedLesson(lesson.id)
        val attempts = (existing?.attempts ?: 0) + 1
        lessonDao.insertCompletedLesson(
            CompletedLessonEntity(
                lessonId = lesson.id,
                attempts = attempts,
                codeSubmitted = code,
                timeSpentSeconds = timeSpent
            )
        )
        if (existing == null) {
            awardXp(lesson.xpReward)
            awardSunCoins(lesson.sunCoinReward)
            awardWaterDrops(lesson.waterDropReward)
            progressDao.upsertProgress(
                (progressDao.getProgress() ?: UserProgressEntity()).copy(
                    totalLessonsCompleted = lessonDao.getCompletedCount()
                )
            )
            lesson.badgeReward?.let { awardBadge(it) }
            return true
        }
        return false
    }

    // --- Badges ---

    fun observeBadges(): Flow<List<BadgeEntity>> = badgeDao.observeBadges()

    suspend fun awardBadge(badgeId: String) {
        badgeDao.insertBadge(BadgeEntity(badgeId = badgeId))
    }

    suspend fun hasBadge(badgeId: String): Boolean = badgeDao.getBadge(badgeId) != null

    // --- Workspaces ---

    fun observeWorkspaces(): Flow<List<WorkspaceEntity>> = workspaceDao.observeWorkspaces()

    suspend fun saveWorkspace(workspace: WorkspaceEntity): Long = workspaceDao.upsertWorkspace(workspace)
    suspend fun getWorkspace(id: Long): WorkspaceEntity? = workspaceDao.getWorkspace(id)
    suspend fun deleteWorkspace(id: Long) = workspaceDao.deleteById(id)

    // --- Code History ---

    fun observeCodeHistory(limit: Int = 50): Flow<List<CodeHistoryEntity>> =
        codeHistoryDao.observeRecentHistory(limit)

    suspend fun saveCodeHistory(entry: CodeHistoryEntity): Long =
        codeHistoryDao.insertHistory(entry)

    // --- Streak ---

    suspend fun updateStreak() {
        val progress = progressDao.getProgress() ?: return
        val today = System.currentTimeMillis() / (24 * 60 * 60 * 1000)
        val lastDay = progress.lastActiveDate / (24 * 60 * 60 * 1000)
        val newStreak = when {
            lastDay == today -> progress.currentStreak
            lastDay == today - 1 -> progress.currentStreak + 1
            else -> 1
        }
        val newBest = maxOf(newStreak, progress.bestStreak)
        progressDao.upsertProgress(
            progress.copy(
                currentStreak = newStreak,
                bestStreak = newBest,
                lastActiveDate = System.currentTimeMillis()
            )
        )
    }

    // --- Professional Mode ---

    suspend fun checkProfessionalModeUnlock(): Boolean {
        val progress = progressDao.getProgress() ?: return false
        val completed = lessonDao.getCompletedCount()
        if (progress.level >= 10 || completed >= 50) {
            dataStore.edit { it[KEY_PRO_MODE_UNLOCKED] = true }
            return true
        }
        return false
    }

    // --- Reset ---

    suspend fun resetAllProgress() {
        dataStore.edit { it.clear() }
        progressDao.upsertProgress(UserProgressEntity())
        codeHistoryDao.clearHistory()
    }
}