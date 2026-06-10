package com.consica.code.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.consica.code.R
import com.consica.code.data.model.AgeGroup
import com.consica.code.ui.components.TerraAvatar
import com.consica.code.ui.components.TerraDialogueBubble
import com.consica.code.ui.components.BiomeNode
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeMapScreen(
    onLessonClick: (String) -> Unit,
    onPlaygroundClick: () -> Unit,
    onPathClick: () -> Unit,
    onWorkspaceClick: () -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ForestGreen.copy(alpha = 0.08f),
                        LeafLight,
                        CleanWhite
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopAppBar(
            title = {
                Text(
                    text = "Consica Code",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            actions = {
                IconButton(onClick = onWorkspaceClick) {
                    Icon(Icons.Filled.Folder, contentDescription = stringResource(R.string.nav_workspace))
                }
                IconButton(onClick = { /* Profile */ }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Terra greeting
        TerraDialogueBubble(
            message = when {
                isOffline -> "We're offline, but your code garden is still growing! 🦉"
                profile.ageGroup == AgeGroup.YOUNG -> "Hoot hoot! 🌱 Let's grow some amazing code today!"
                profile.ageGroup == AgeGroup.TEEN -> "Welcome back! Your learning journey continues. 🌿"
                else -> "Ready to build. Your workspace is waiting."
            },
            expression = if (isOffline) "concerned" else "happy"
        )

        Spacer(Modifier.height(8.dp))

        // Stats row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = CardShape,
            colors = CardDefaults.cardColors(containerColor = CleanWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    icon = "🌟",
                    value = "${progress?.totalXp ?: 0}",
                    label = stringResource(R.string.home_xp),
                    color = SunYellowDark
                )
                StatItem(
                    icon = "🪙",
                    value = "${progress?.sunCoins ?: 0}",
                    label = stringResource(R.string.home_sun_coins),
                    color = SunYellow
                )
                StatItem(
                    icon = "💧",
                    value = "${progress?.waterDrops ?: 0}",
                    label = stringResource(R.string.home_water_drops),
                    color = RiverBlue
                )
                StatItem(
                    icon = "🔥",
                    value = "${progress?.currentStreak ?: 0}",
                    label = stringResource(R.string.home_streak),
                    color = Color(0xFFFF6D00)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Quick actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Filled.Code,
                label = stringResource(R.string.nav_playground),
                onClick = onPlaygroundClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Icons.Filled.Extension,
                label = stringResource(R.string.nav_puzzles),
                onClick = {
                    // Navigate to puzzles
                },
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Icons.Filled.EmojiEvents,
                label = stringResource(R.string.nav_rewards),
                onClick = {
                    // Navigate to rewards
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Biome Map Section
        Text(
            text = "Your Biome Map",
            style = MaterialTheme.typography.titleMedium,
            color = SoilDark,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        // Learning nodes grid
        val nodes = listOf(
            BiomeNodeData("Forest Floor", "🌱", true, "seed_001", true),
            BiomeNodeData("Sprout Hill", "🌿", true, "lesson_002", false),
            BiomeNodeData("HTML Orchard", "🌳", true, "lesson_003", false),
            BiomeNodeData("Python Pond", "🐍", true, "lesson_004", false),
            BiomeNodeData("CSS Meadow", "🌸", false, "lesson_005", false),
            BiomeNodeData("Code Mountain", "⛰️", false, "lesson_006", false),
            BiomeNodeData("Debug Forest", "🔧", false, "lesson_007", false),
            BiomeNodeData("Logic Lake", "💡", false, "lesson_008", false)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            nodes.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { node ->
                        BiomeNode(
                            title = node.title,
                            emoji = node.emoji,
                            isUnlocked = node.isUnlocked,
                            isActive = node.isActive,
                            isCompleted = node.isCompleted,
                            onClick = { if (node.isUnlocked) onLessonClick(node.lessonId) },
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .clickable(enabled = node.isUnlocked) { onLessonClick(node.lessonId) }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Continue learning
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onLessonClick("seed_001") },
            shape = CardShape,
            colors = CardDefaults.cardColors(
                containerColor = ForestGreenContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.home_continue),
                        style = MaterialTheme.typography.titleSmall,
                        color = ForestGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Planting a Seed — your first HTML heading!",
                        style = MaterialTheme.typography.bodySmall,
                        color = ForestGreen
                    )
                }
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

data class BiomeNodeData(
    val title: String,
    val emoji: String,
    val isUnlocked: Boolean,
    val lessonId: String,
    val isActive: Boolean,
    val isCompleted: Boolean = false
)

@Composable
fun StatItem(icon: String, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 22.sp)
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SoilDark
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = StoneGray
        )
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CleanWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = ForestGreen,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SoilDark,
                textAlign = TextAlign.Center
            )
        }
    }
}