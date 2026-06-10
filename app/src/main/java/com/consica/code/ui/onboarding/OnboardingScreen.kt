package com.consica.code.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.consica.code.R
import com.consica.code.data.model.*
import com.consica.code.ui.theme.*
import com.consica.code.ui.components.TerraAvatar

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val selectedAgeGroup by viewModel.selectedAgeGroup.collectAsState()
    val selectedGoal by viewModel.selectedGoal.collectAsState()
    val selectedExperience by viewModel.selectedExperience.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()
    val selectedInterests by viewModel.selectedInterests.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LeafLight, CleanWhite, LeafLight)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        // Progress indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(6) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (index == currentStep) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                index < currentStep -> ForestGreen
                                index == currentStep -> ForestGreen
                                else -> StoneGrayLight
                            }
                        )
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Terra Avatar
        TerraAvatar(
            expression = when (currentStep) {
                0 -> "excited"
                5 -> "celebrating"
                else -> "happy"
            },
            size = 96.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Step Content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
                }
            }
        ) { step ->
            when (step) {
                0 -> AgeStep(
                    selectedAgeGroup = selectedAgeGroup,
                    onSelect = { viewModel.setAgeGroup(it) }
                )
                1 -> GoalStep(
                    selectedGoal = selectedGoal,
                    onSelect = { viewModel.setGoal(it) }
                )
                2 -> ExperienceStep(
                    selectedExperience = selectedExperience,
                    onSelect = { viewModel.setExperience(it) }
                )
                3 -> ThemeStep(
                    selectedTheme = selectedTheme,
                    onSelect = { viewModel.setTheme(it) }
                )
                4 -> InterestsStep(
                    selectedInterests = selectedInterests,
                    onToggle = { viewModel.toggleInterest(it) }
                )
                5 -> SummaryStep(
                    ageGroup = selectedAgeGroup,
                    goal = selectedGoal,
                    experience = selectedExperience,
                    theme = selectedTheme,
                    interests = selectedInterests
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = { viewModel.previousStep() },
                    modifier = Modifier.weight(1f),
                    shape = PillButtonShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ForestGreen
                    ),
                    border = BorderStroke(2.dp, ForestGreen)
                ) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.onboarding_back))
                }
            }

            Button(
                onClick = {
                    if (currentStep < 5) {
                        viewModel.nextStep()
                    } else {
                        viewModel.completeOnboarding()
                        onComplete()
                    }
                },
                modifier = Modifier.weight(1f),
                shape = PillButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text(
                    if (currentStep < 5) stringResource(R.string.onboarding_next)
                    else stringResource(R.string.onboarding_start),
                    color = TextOnPrimary
                )
                if (currentStep == 5) {
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = TextOnPrimary)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun AgeStep(selectedAgeGroup: AgeGroup, onSelect: (AgeGroup) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.onboarding_age_title),
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_age_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        AgeGroup.entries.forEach { group ->
            val isSelected = group == selectedAgeGroup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onSelect(group) },
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ForestGreenContainer else CleanWhite
                ),
                border = if (isSelected) BorderStroke(2.dp, ForestGreen) else BorderStroke(1.dp, StoneGrayLight)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (group) {
                            AgeGroup.YOUNG -> Icons.Filled.Park
                            AgeGroup.TEEN -> Icons.Filled.Forest
                            AgeGroup.YOUNG_ADULT -> Icons.Filled.Landscape
                        },
                        contentDescription = null,
                        tint = if (isSelected) ForestGreen else StoneGray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = when (group) {
                                AgeGroup.YOUNG -> stringResource(R.string.age_group_8_12)
                                AgeGroup.TEEN -> stringResource(R.string.age_group_13_16)
                                AgeGroup.YOUNG_ADULT -> stringResource(R.string.age_group_16_plus)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = SoilDark,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = when (group) {
                                AgeGroup.YOUNG -> stringResource(R.string.age_group_8_12_desc)
                                AgeGroup.TEEN -> stringResource(R.string.age_group_13_16_desc)
                                AgeGroup.YOUNG_ADULT -> stringResource(R.string.age_group_16_plus_desc)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = StoneGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GoalStep(selectedGoal: LearningGoal, onSelect: (LearningGoal) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.onboarding_goal_title),
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_goal_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        val goalIcons = mapOf(
            LearningGoal.PYTHON to Icons.Filled.DataObject,
            LearningGoal.WEB to Icons.Filled.Web,
            LearningGoal.GAMES to Icons.Filled.SportsEsports,
            LearningGoal.APPS to Icons.Filled.PhoneAndroid,
            LearningGoal.EXPLORE to Icons.Filled.Explore,
            LearningGoal.CREATIVE to Icons.Filled.Palette
        )
        LearningGoal.entries.forEach { goal ->
            val isSelected = goal == selectedGoal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onSelect(goal) },
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) RiverBlue.copy(alpha = 0.1f) else CleanWhite
                ),
                border = if (isSelected) BorderStroke(2.dp, RiverBlue) else BorderStroke(1.dp, StoneGrayLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = goalIcons[goal] ?: Icons.Filled.Code,
                        contentDescription = null,
                        tint = if (isSelected) RiverBlue else StoneGray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Text(
                        text = goal.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isSelected) RiverBlueDark else SoilDark,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceStep(selectedExperience: ExperienceLevel, onSelect: (ExperienceLevel) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.onboarding_experience_title),
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_experience_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        ExperienceLevel.entries.forEach { level ->
            val isSelected = level == selectedExperience
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onSelect(level) },
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MossGreen.copy(alpha = 0.1f) else CleanWhite
                ),
                border = if (isSelected) BorderStroke(2.dp, MossGreen) else BorderStroke(1.dp, StoneGrayLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (level) {
                            ExperienceLevel.BEGINNER -> Icons.Filled.SelfImprovement
                            ExperienceLevel.SOME -> Icons.Filled.Hiking
                            ExperienceLevel.INTERMEDIATE -> Icons.Filled.Terrain
                            ExperienceLevel.ADVANCED -> Icons.Filled.Rocket
                        },
                        contentDescription = null,
                        tint = if (isSelected) MossGreen else StoneGray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = level.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isSelected) ForestGreenDark else SoilDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = when (level) {
                                ExperienceLevel.BEGINNER -> stringResource(R.string.experience_beginner_desc)
                                ExperienceLevel.SOME -> stringResource(R.string.experience_some_desc)
                                ExperienceLevel.INTERMEDIATE -> stringResource(R.string.experience_intermediate_desc)
                                ExperienceLevel.ADVANCED -> stringResource(R.string.experience_advanced_desc)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = StoneGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeStep(selectedTheme: ThemeIntensity, onSelect: (ThemeIntensity) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.onboarding_theme_title),
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_theme_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        ThemeIntensity.entries.forEach { theme ->
            val isSelected = theme == selectedTheme
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onSelect(theme) },
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SunYellowContainer else CleanWhite
                ),
                border = if (isSelected) BorderStroke(2.dp, SunYellow) else BorderStroke(1.dp, StoneGrayLight)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (theme) {
                            ThemeIntensity.PLAYFUL -> "🌱"
                            ThemeIntensity.BALANCED -> "🌿"
                            ThemeIntensity.FOCUSED -> "🪴"
                        },
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isSelected) SoilDark else SoilDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Text(
                            text = when (theme) {
                                ThemeIntensity.PLAYFUL -> stringResource(R.string.theme_playful_desc)
                                ThemeIntensity.BALANCED -> stringResource(R.string.theme_balanced_desc)
                                ThemeIntensity.FOCUSED -> stringResource(R.string.theme_focused_desc)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = StoneGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InterestsStep(
    selectedInterests: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.onboarding_interests_title),
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_interests_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        val interestOptions = listOf(
            "python" to stringResource(R.string.interest_python),
            "web" to stringResource(R.string.interest_web),
            "games" to stringResource(R.string.interest_games),
            "apps" to stringResource(R.string.interest_apps),
            "ai" to stringResource(R.string.interest_ai),
            "creative" to stringResource(R.string.interest_creative)
        )
        val interestIcons = mapOf(
            "python" to "🐍",
            "web" to "🌐",
            "games" to "🎮",
            "apps" to "📱",
            "ai" to "🤖",
            "creative" to "🎨"
        )
        Column {
            interestOptions.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { (key, label) ->
                        val isSelected = key in selectedInterests
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp)
                                .clickable { onToggle(key) },
                            shape = CardShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ForestGreenContainer else CleanWhite
                            ),
                            border = if (isSelected) BorderStroke(2.dp, ForestGreen)
                            else BorderStroke(1.dp, StoneGrayLight)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(interestIcons[key] ?: "📚", fontSize = 28.sp)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) ForestGreen else SoilDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryStep(
    ageGroup: AgeGroup,
    goal: LearningGoal,
    experience: ExperienceLevel,
    theme: ThemeIntensity,
    interests: Set<String>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Ready to grow! 🌿",
            style = MaterialTheme.typography.headlineMedium,
            color = SoilDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Here's a summary of your learning journey setup.",
            style = MaterialTheme.typography.bodyLarge,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Card(
            shape = CardShape,
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = BorderStroke(1.dp, StoneGrayLight)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                SummaryRow("Age Group", ageGroup.label)
                SummaryRow("Goal", goal.label)
                SummaryRow("Experience", experience.label)
                SummaryRow("Theme", theme.label)
                if (interests.isNotEmpty()) {
                    SummaryRow("Interests", interests.joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } })
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "You can change these anytime in Settings. Terra can't wait to start growing code with you! 🦉✨",
            style = MaterialTheme.typography.bodyMedium,
            color = StoneGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = StoneGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = SoilDark
        )
    }
}
