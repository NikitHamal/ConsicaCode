package com.consica.code.domain

import com.consica.code.data.model.AgeGroup
import com.consica.code.data.model.TerraDialogue

enum class TerraExpression(val emoji: String, val animationKey: String) {
    HAPPY("🦉", "happy"),
    EXCITED("✨🦉", "excited"),
    THINKING("🤔🦉", "thinking"),
    CONFUSED("❓🦉", "confused"),
    PROUD("🌟🦉", "proud"),
    SLEEPY("😴🦉", "sleepy"),
    FOCUSED("🎯🦉", "focused"),
    PROFESSIONAL("📚🦉", "professional"),
    ENCOURAGING("💚🦉", "encouraging"),
    CELEBRATING("🎉🦉", "celebrating"),
    CONCERNED("💧🦉", "concerned")
}

data class CharacterState(
    val expression: TerraExpression = TerraExpression.HAPPY,
    val message: String = "",
    val isTyping: Boolean = false,
    val isMinimized: Boolean = false,
    val visible: Boolean = true
)

class CharacterGuide {
    fun getGreeting(ageGroup: AgeGroup, hourOfDay: Int = 12): String = when {
        ageGroup == AgeGroup.YOUNG && hourOfDay < 10 -> "Hoot hoot! 🌅 Ready for some morning coding magic?"
        ageGroup == AgeGroup.YOUNG && hourOfDay < 17 -> "Hey there, explorer! 🦉 Ready to grow some code today?"
        ageGroup == AgeGroup.YOUNG -> "Evening hoots! 🌙 Perfect time for a coding adventure!"
        ageGroup == AgeGroup.TEEN && hourOfDay < 10 -> "Morning! Let's build something awesome today."
        ageGroup == AgeGroup.TEEN && hourOfDay < 17 -> "Ready to level up? Your coding journey continues."
        ageGroup == AgeGroup.TEEN -> "Evening session! Great time to focus on coding."
        hourOfDay < 10 -> "Good morning. Ready to continue your professional practice."
        hourOfDay < 17 -> "Welcome back. Your workspace is ready."
        else -> "Evening. A productive coding session awaits."
    }

    fun getOfflineMessage(ageGroup: AgeGroup): String = when (ageGroup) {
        AgeGroup.YOUNG -> "We're playing offline today, but don't worry! 🦉 Everything you make is safely saved right here on your device. Terra's got your back!"
        AgeGroup.TEEN -> "You're offline, but all progress is saved locally. Continue coding normally."
        AgeGroup.YOUNG_ADULT -> "Offline mode active. Local storage is persisting all changes."
    }

    fun getSuccessMessage(ageGroup: AgeGroup, xp: Int, badge: String?): String = when {
        ageGroup == AgeGroup.YOUNG && badge != null ->
            "WOOHOO! 🌱 You earned the \"$badge\" badge and $xp XP! Your ecosystem is blooming!"
        ageGroup == AgeGroup.YOUNG ->
            "Amazing job! 🌟 You earned $xp XP! Keep growing!"
        ageGroup == AgeGroup.TEEN ->
            "Nice work! +$xp XP earned. Your skills are growing steadily."
        else ->
            "Completed. +$xp XP. Progress recorded."
    }

    fun getErrorHint(ageGroup: AgeGroup, dialogue: TerraDialogue): String = when (ageGroup) {
        AgeGroup.YOUNG -> dialogue.errorYoung
        AgeGroup.TEEN -> dialogue.errorTeen
        AgeGroup.YOUNG_ADULT -> dialogue.errorAdult
    }

    fun getHintMessage(ageGroup: AgeGroup, dialogue: TerraDialogue): String = when (ageGroup) {
        AgeGroup.YOUNG -> dialogue.hintYoung
        AgeGroup.TEEN -> dialogue.hintTeen
        AgeGroup.YOUNG_ADULT -> dialogue.hintAdult
    }

    fun getEncouragement(ageGroup: AgeGroup, dialogue: TerraDialogue): String = when (ageGroup) {
        AgeGroup.YOUNG -> dialogue.encouragementYoung
        AgeGroup.TEEN -> dialogue.encouragementTeen
        AgeGroup.YOUNG_ADULT -> dialogue.encouragementAdult
    }

    fun getLessonExplanation(ageGroup: AgeGroup, dialogue: TerraDialogue): String = when (ageGroup) {
        AgeGroup.YOUNG -> dialogue.youngExplanation
        AgeGroup.TEEN -> dialogue.teenExplanation
        AgeGroup.YOUNG_ADULT -> dialogue.adultExplanation
    }

    fun shouldShowAnimations(ageGroup: AgeGroup, themeIntensity: String): Boolean =
        ageGroup == AgeGroup.YOUNG || themeIntensity == "playful"

    fun shouldMinimizeByDefault(ageGroup: AgeGroup): Boolean =
        ageGroup == AgeGroup.YOUNG_ADULT
}
