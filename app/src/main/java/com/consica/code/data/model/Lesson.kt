package com.consica.code.data.model

data class Lesson(
    val id: String,
    val title: String,
    val description: String,
    val category: LessonCategory,
    val difficulty: LessonDifficulty,
    val ageGroup: AgeGroup,
    val order: Int,
    val estimatedMinutes: Int,
    val prerequisiteLessonIds: List<String> = emptyList(),
    val unlockLevel: Int = 1,
    val terraDialogue: TerraDialogue,
    val codeChallenge: CodeChallenge,
    val xpReward: Int = 50,
    val sunCoinReward: Int = 5,
    val waterDropReward: Int = 3,
    val badgeReward: String? = null,
    val ecosystemReward: String? = null
)

enum class LessonCategory(val key: String, val displayName: String) {
    HTML_BASICS("html_basics", "HTML Basics"),
    HTML_STRUCTURE("html_structure", "HTML Structure"),
    PYTHON_BASICS("python_basics", "Python Basics"),
    PYTHON_LOGIC("python_logic", "Python Logic"),
    CSS_FUNDAMENTALS("css_fundamentals", "CSS Fundamentals"),
    WEB_PROJECTS("web_projects", "Web Projects"),
    DEBUGGING("debugging", "Debugging"),
    ALGORITHMS("algorithms", "Algorithms"),
    CREATIVE("creative", "Creative Coding"),
    PROFESSIONAL("professional", "Professional Skills")
}

enum class LessonDifficulty(val key: String, val stars: Int) {
    BEGINNER("beginner", 1),
    EASY("easy", 1),
    MEDIUM("medium", 2),
    HARD("hard", 3),
    ADVANCED("advanced", 4),
    EXPERT("expert", 5)
}

data class TerraDialogue(
    val youngExplanation: String,
    val teenExplanation: String,
    val adultExplanation: String,
    val encouragementYoung: String = "You're doing great! 🌱",
    val encouragementTeen: String = "Nice work! Keep it up.",
    val encouragementAdult: String = "Well done. Continue.",
    val hintYoung: String = "Try thinking about it like planting a seed!",
    val hintTeen: String = "Check your syntax and structure.",
    val hintAdult: String = "Review the logic — there may be an error in the implementation.",
    val errorYoung: String = "Oops! A little bug snuck in. Let's fix it together!",
    val errorTeen: String = "There's an error in your code. Let's debug it.",
    val errorAdult: String = "Error detected. Check the output for details."
)

data class CodeChallenge(
    val language: CodeLanguage,
    val starterCode: String,
    val solutionPattern: String = "",
    val expectedOutput: String = "",
    val hints: List<String> = emptyList(),
    val dragDropBlocks: List<String> = emptyList(),
    val dragDropSolution: List<Int> = emptyList(),
    val isPuzzle: Boolean = false
)

enum class CodeLanguage(val key: String, val displayName: String) {
    HTML("html", "HTML"),
    PYTHON("python", "Python"),
    CSS("css", "CSS")
}

// Prebuilt lesson library
object LessonLibrary {
    fun getSeedLesson(): Lesson = Lesson(
        id = "seed_001",
        title = "Planting a Seed",
        description = "Write your first HTML heading and watch a seed sprout!",
        category = LessonCategory.HTML_BASICS,
        difficulty = LessonDifficulty.BEGINNER,
        ageGroup = AgeGroup.YOUNG,
        order = 1,
        estimatedMinutes = 5,
        terraDialogue = TerraDialogue(
            youngExplanation = "Hoot hoot! 🌱 Today we're going to plant our very first code seed! An HTML heading is like a big, bold title for a webpage. It tells everyone what's important. Let's write one together!",
            teenExplanation = "Let's start with HTML headings. They're used to structure content on web pages. The h1 tag creates the largest, most important heading.",
            adultExplanation = "HTML headings define the hierarchical structure of web content. The h1 element represents the highest-level heading. We'll create one to begin."
        ),
        codeChallenge = CodeChallenge(
            language = CodeLanguage.HTML,
            starterCode = "<!-- Write an h1 heading with the word \"Sprout\" -->\n\n",
            solutionPattern = "<h1>Sprout</h1>",
            hints = listOf(
                "Use <h1> to start and </h1> to end",
                "Put your word between the opening and closing tags"
            )
        ),
        xpReward = 50,
        sunCoinReward = 10,
        waterDropReward = 5,
        badgeReward = "first_sprout",
        ecosystemReward = "tiny_sprout"
    )

    fun getDefaultLessons(): List<Lesson> = listOf(getSeedLesson())
}
