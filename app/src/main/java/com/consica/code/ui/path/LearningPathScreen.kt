package com.consica.code.ui.path

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.ui.theme.*

data class PathLesson(
    val id: String,
    val title: String,
    val emoji: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathScreen(
    onLessonClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val paths = remember {
        mapOf(
            stringResource(R.string.path_beginner_web) to listOf(
                PathLesson("seed_001", "Planting a Seed", "🌱", true, false, "HTML"),
                PathLesson("lesson_002", "What is the Web?", "🌐", true, false, "Concepts"),
                PathLesson("lesson_003", "HTML Tags", "🏷️", true, false, "HTML"),
                PathLesson("lesson_004", "Headings & Paragraphs", "📝", true, false, "HTML"),
                PathLesson("lesson_005", "Links & Images", "🔗", false, false, "HTML"),
                PathLesson("lesson_006", "HTML Lists", "📋", false, false, "HTML"),
                PathLesson("lesson_007", "Simple CSS", "🎨", false, false, "CSS"),
                PathLesson("lesson_008", "Mini Web Page", "🌟", false, false, "Project")
            ),
            stringResource(R.string.path_beginner_python) to listOf(
                PathLesson("py_001", "Hello Python", "🐍", true, false, "Python"),
                PathLesson("py_002", "Variables", "📦", true, false, "Python"),
                PathLesson("py_003", "Strings & Numbers", "🔢", false, false, "Python"),
                PathLesson("py_004", "User Input", "⌨️", false, false, "Python"),
                PathLesson("py_005", "If/Else", "🔀", false, false, "Logic"),
                PathLesson("py_006", "Loops", "🔁", false, false, "Logic"),
                PathLesson("py_007", "Lists", "📋", false, false, "Data"),
                PathLesson("py_008", "Functions", "🔧", false, false, "Python"),
                PathLesson("py_009", "Debugging", "🔍", false, false, "Skills"),
                PathLesson("py_010", "Mini Game", "🎮", false, false, "Project")
            ),
            stringResource(R.string.path_intermediate) to listOf(
                PathLesson("int_001", "CSS Layouts", "🎨", false, false, "CSS"),
                PathLesson("int_002", "HTML Forms", "📧", false, false, "HTML"),
                PathLesson("int_003", "Python Logic Puzzles", "🧩", false, false, "Python"),
                PathLesson("int_004", "Data Structures", "🏗️", false, false, "Data"),
                PathLesson("int_005", "Functions & Modules", "🧰", false, false, "Python")
            ),
            stringResource(R.string.path_advanced) to listOf(
                PathLesson("adv_001", "Professional Editor", "💻", false, false, "Tools"),
                PathLesson("adv_002", "Multi-File Projects", "📁", false, false, "Projects"),
                PathLesson("adv_003", "Algorithmic Thinking", "🧠", false, false, "Algorithms"),
                PathLesson("adv_004", "Clean Code", "✨", false, false, "Skills"),
                PathLesson("adv_005", "Portfolio Project", "🏆", false, false, "Project"),
                PathLesson("adv_006", "Capstone Challenge", "🎓", false, false, "Challenge")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LeafLight)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.path_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        paths.forEach { (sectionName, lessons) ->
            Text(
                text = sectionName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            lessons.forEach { lesson ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable(enabled = lesson.isUnlocked) { onLessonClick(lesson.id) },
                    shape = CardShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            lesson.isCompleted -> ForestGreenContainer.copy(alpha = 0.3f)
                            lesson.isUnlocked -> CleanWhite
                            else -> StoneGrayLight.copy(alpha = 0.15f)
                        }
                    ),
                    border = if (lesson.isUnlocked && !lesson.isCompleted)
                        androidx.compose.foundation.BorderStroke(1.dp, StoneGrayLight)
                    else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lesson.isUnlocked) lesson.emoji else "🔒",
                            fontSize = androidx.compose.ui.unit.sp.times(24)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (lesson.isUnlocked) SoilDark else StoneGray,
                                fontWeight = if (lesson.isCompleted) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = lesson.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (lesson.isUnlocked) StoneGray else StoneGray.copy(alpha = 0.5f)
                            )
                        }
                        if (lesson.isCompleted) {
                            Text("✓", color = ForestGreen, fontWeight = FontWeight.Bold)
                        } else if (lesson.isUnlocked) {
                            Icon(
                                Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.scale(0.9f)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(32.dp))
    }
}
