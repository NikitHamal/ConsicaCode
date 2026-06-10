package com.consica.code.data.model

enum class AgeGroup(val minAge: Int, val maxAge: Int, val label: String) {
    YOUNG(8, 12, "8–12"),
    TEEN(13, 16, "13–16"),
    YOUNG_ADULT(17, 19, "16+");

    companion object {
        fun fromAge(age: Int): AgeGroup = when {
            age <= 12 -> YOUNG
            age <= 16 -> TEEN
            else -> YOUNG_ADULT
        }
    }
}

enum class LearningGoal(val key: String, val label: String) {
    PYTHON("python", "Learn Python"),
    WEB("web", "Build Websites"),
    GAMES("games", "Make Games"),
    APPS("apps", "Build Apps"),
    EXPLORE("explore", "Explore Everything"),
    CREATIVE("creative", "Creative Coding")
}

enum class ExperienceLevel(val key: String, val label: String) {
    BEGINNER("beginner", "New to coding"),
    SOME("some", "Some experience"),
    INTERMEDIATE("intermediate", "Comfortable coder"),
    ADVANCED("advanced", "Experienced")
}

enum class ThemeIntensity(val key: String, val label: String) {
    PLAYFUL("playful", "Playful"),
    BALANCED("balanced", "Balanced"),
    FOCUSED("focused", "Focused")
}

data class UserProfile(
    val ageGroup: AgeGroup = AgeGroup.YOUNG,
    val age: Int = 10,
    val learningGoal: LearningGoal = LearningGoal.EXPLORE,
    val experienceLevel: ExperienceLevel = ExperienceLevel.BEGINNER,
    val themeIntensity: ThemeIntensity = ThemeIntensity.PLAYFUL,
    val interests: Set<String> = emptySet(),
    val onboardingComplete: Boolean = false,
    val professionalModeUnlocked: Boolean = false
)
