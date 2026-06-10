package com.consica.code.ui.rewards

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.BorderStroke
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
import com.consica.code.data.local.entity.*
import com.consica.code.data.model.*
import com.consica.code.data.repository.AppRepository
import com.consica.code.ConsicaCodeApp
import com.consica.code.dataStore
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    onBack: () -> Unit
) {
    val app = remember { ConsicaCodeApp.instance }
    val repository = remember { AppRepository(app.database, app.dataStore) }
    val badges by repository.observeBadges().collectAsState(initial = emptyList())
    val progress by repository.userProgress.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SunYellowContainer.copy(alpha = 0.3f),
                        LeafLight,
                        CleanWhite
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.rewards_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Progress overview
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = ForestGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatColumn("🌟", "${progress?.totalXp ?: 0}", "XP")
                    StatColumn("🏆", "${badges.size}", "Badges")
                    StatColumn("📊", "Level ${progress?.level ?: 1}", "Level")
                    StatColumn("🔥", "${progress?.currentStreak ?: 0}", "Streak")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Progress bar
            val nextLevelXp = (progress?.level ?: 1) * 100 + 50
            val currentXp = (progress?.totalXp ?: 0) % nextLevelXp
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress to Level ${(progress?.level ?: 1) + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            color = StoneGray
                        )
                        Text(
                            text = "$currentXp / $nextLevelXp XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = currentXp.toFloat() / nextLevelXp.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = ForestGreen,
                        trackColor = ForestGreenContainer.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Badges
            Text(
                text = "Your Badges",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoilDark
            )
            Spacer(Modifier.height(12.dp))

            val allBadges = listOf(
                BadgeDisplay("first_sprout", "🌱", stringResource(R.string.badge_first_sprout), "Completed your first lesson!"),
                BadgeDisplay("code_seedling", "🌿", stringResource(R.string.badge_code_seedling), "Wrote 5 pieces of code"),
                BadgeDisplay("html_planter", "🏷️", stringResource(R.string.badge_html_planter), "Mastered HTML basics"),
                BadgeDisplay("python_bloom", "🐍", stringResource(R.string.badge_python_bloom), "Ran your first Python code"),
                BadgeDisplay("bug_squasher", "🔧", stringResource(R.string.badge_bug_squasher), "Fixed 3 errors"),
                BadgeDisplay("puzzle_master", "🧩", stringResource(R.string.badge_puzzle_master), "Solved 10 puzzles"),
                BadgeDisplay("streak_warrior", "🔥", stringResource(R.string.badge_streak_warrior), "7-day streak"),
                BadgeDisplay("web_gardener", "🌐", stringResource(R.string.badge_web_gardener), "Built 3 web pages"),
                BadgeDisplay("code_forester", "🌳", stringResource(R.string.badge_code_forester), "Completed 20 lessons"),
                BadgeDisplay("ecosystem_guardian", "💚", stringResource(R.string.badge_ecosystem_guardian), "Unlocked all biomes"),
                BadgeDisplay("professional_coder", "📚", stringResource(R.string.badge_professional_coder), "Unlocked professional mode")
            )

            val earnedBadgeIds = badges.map { it.badgeId }.toSet()

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(380.dp)
            ) {
                items(allBadges) { badge ->
                    val earned = badge.id in earnedBadgeIds
                    Card(
                        shape = CardShape,
                        colors = CardDefaults.cardColors(
                            containerColor = if (earned) CleanWhite else StoneGrayLight.copy(alpha = 0.2f)
                        ),
                        border = if (earned) null
                        else BorderStroke(1.dp, StoneGray.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (earned) badge.emoji else "🔒",
                                fontSize = 28.sp
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = badge.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (earned) SoilDark else StoneGray,
                                textAlign = TextAlign.Center,
                                fontWeight = if (earned) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

data class BadgeDisplay(
    val id: String,
    val emoji: String,
    val name: String,
    val description: String
)

@Composable
fun StatColumn(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 24.sp)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
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
