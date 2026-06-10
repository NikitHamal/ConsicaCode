package com.consica.code.data.model

data class UserProgress(
    val totalXp: Int = 0,
    val level: Int = 1,
    val sunCoins: Int = 0,
    val waterDrops: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastActiveDate: Long = 0,
    val totalLessonsCompleted: Int = 0,
    val totalPuzzlesCompleted: Int = 0,
    val totalProjectsCompleted: Int = 0,
    val unlockedBiomes: Set<String> = emptySet(),
    val ecosystemItems: Set<String> = emptySet(),
    val earnedBadges: Set<String> = emptySet(),
    val completedLessonIds: Set<String> = emptySet(),
    val completedPuzzleIds: Set<String> = emptySet()
) {
    val xpForNextLevel: Int get() = level * 100 + 50
    val levelProgress: Float get() = (totalXp % xpForNextLevel).toFloat() / xpForNextLevel
    val masteryLevel: Int get() = level / 5
}

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val category: BadgeCategory,
    val requiredXp: Int = 0,
    val requiredLevel: Int = 0,
    val requiredStreak: Int = 0,
    val requiredLessonsCompleted: Int = 0
)

enum class BadgeCategory(val key: String) {
    PROGRESS("progress"),
    MASTERY("mastery"),
    STREAK("streak"),
    EXPLORER("explorer"),
    CHALLENGE("challenge"),
    PROFESSIONAL("professional")
}

data class EcosystemState(
    val biomeName: String = "Forest Floor",
    val plantsUnlocked: Int = 0,
    val animalsUnlocked: Int = 0,
    val waterLevel: Float = 0f,
    val sunlightLevel: Float = 0f,
    val decorations: Set<String> = emptySet(),
    val totalGrowth: Int = 0
)
