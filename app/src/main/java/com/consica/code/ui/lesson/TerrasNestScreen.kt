package com.consica.code.ui.lesson

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.consica.code.R
import com.consica.code.data.model.*
import com.consica.code.domain.CharacterGuide
import com.consica.code.domain.TerraExpression
import com.consica.code.ui.components.TerraAvatar
import com.consica.code.ui.components.TerraDialogueBubble
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerrasNestScreen(
    lessonId: String,
    onStartCoding: () -> Unit,
    onBack: () -> Unit,
    viewModel: LessonViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val lesson = remember(lessonId) { LessonLibrary.getSeedLesson() }
    val guide = remember { CharacterGuide() }
    val explanation = remember(profile.ageGroup, lesson) {
        guide.getLessonExplanation(profile.ageGroup, lesson.terraDialogue)
    }
    val showContent by viewModel.showContent.collectAsState()

    LaunchedEffect(lessonId) {
        viewModel.startReveal()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20).copy(alpha = 0.06f),
                        LeafLight,
                        CleanWhite
                    )
                )
            )
    ) {
        TopAppBar(
            title = { Text(lesson.title) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Terra's Nest — big Terra with expression
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = ForestGreenContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TerraAvatar(
                        expression = "happy",
                        size = 120.dp
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Terra's Nest",
                        style = MaterialTheme.typography.titleMedium,
                        color = ForestGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.a11y_terra_avatar),
                        style = MaterialTheme.typography.bodySmall,
                        color = StoneGray
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Lesson info card
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.MenuBook,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = lesson.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${lesson.difficulty.stars} ★",
                            style = MaterialTheme.typography.labelSmall,
                            color = SunYellowDark
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = SoilDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lesson.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = StoneGray
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AssistChip(
                            onClick = {},
                            label = { Text("${lesson.estimatedMinutes} min", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Filled.Timer, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("+${lesson.xpReward} XP", fontSize = 11.sp) },
                            leadingIcon = {
                                Text("🌟", fontSize = 12.sp)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Explanation from Terra
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { it / 3 }
            ) {
                Column {
                    TerraDialogueBubble(
                        message = explanation,
                        expression = if (profile.ageGroup == AgeGroup.YOUNG) "excited" else "professional",
                        showAvatar = profile.ageGroup != AgeGroup.YOUNG_ADULT || profile.themeIntensity == ThemeIntensity.PLAYFUL
                    )

                    val encouragement = remember(profile.ageGroup, lesson) {
                        guide.getEncouragement(profile.ageGroup, lesson.terraDialogue)
                    }
                    TerraDialogueBubble(
                        message = encouragement,
                        expression = "encouraging",
                        showAvatar = false
                    )

                    Spacer(Modifier.height(24.dp))

                    // Hints section
                    Card(
                        shape = CardShape,
                        colors = CardDefaults.cardColors(
                            containerColor = RiverBlue.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💡 Quick Tips",
                                style = MaterialTheme.typography.labelMedium,
                                color = RiverBlueDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))
                            lesson.codeChallenge.hints.forEach { hint ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("• ", color = RiverBlue)
                                    Text(
                                        text = hint,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SoilDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Start Coding button
            Button(
                onClick = onStartCoding,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = PillButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                enabled = showContent
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Start Coding",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextOnPrimary
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
