package com.consica.code.ui.settings

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.data.model.*
import com.consica.code.data.repository.AppRepository
import com.consica.code.ConsicaCodeApp
import com.consica.code.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val app = remember { ConsicaCodeApp.instance }
    val repository = remember { AppRepository(app.database, app.dataStore) }
    val profile by repository.userProfile.collectAsState(initial = UserProfile())
    val settings by repository.settingsFlow.collectAsState(initial = emptyMap())

    var showResetDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LeafLight)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Appearance
            SettingsGroupHeader("Appearance")

            SettingsToggleItem(
                icon = Icons.Filled.Contrast,
                title = stringResource(R.string.settings_high_contrast),
                subtitle = "Increase contrast for better visibility",
                checked = (settings["high_contrast"] as? Boolean) ?: false,
                onCheckedChange = {
                    scope.launch { repository.updateSetting("high_contrast", it) }
                }
            )

            SettingsToggleItem(
                icon = Icons.Filled.Animation,
                title = stringResource(R.string.settings_reduced_motion),
                subtitle = "Minimize animations",
                checked = (settings["reduced_motion"] as? Boolean) ?: false,
                onCheckedChange = {
                    scope.launch { repository.updateSetting("reduced_motion", it) }
                }
            )

            SettingsToggleItem(
                icon = Icons.Filled.VolumeUp,
                title = stringResource(R.string.settings_sound),
                subtitle = "Play sound effects",
                checked = (settings["sound_enabled"] as? Boolean) ?: true,
                onCheckedChange = {
                    scope.launch { repository.updateSetting("sound_enabled", it) }
                }
            )

            // Learning
            SettingsGroupHeader("Learning Preferences")

            SettingsClickItem(
                icon = Icons.Filled.Person,
                title = stringResource(R.string.settings_age_group),
                subtitle = profile.ageGroup.label,
                onClick = { /* Show age picker dialog */ }
            )

            SettingsClickItem(
                icon = Icons.Filled.Palette,
                title = stringResource(R.string.settings_theme_intensity),
                subtitle = profile.themeIntensity.label,
                onClick = { /* Show theme picker */ }
            )

            SettingsClickItem(
                icon = Icons.Filled.Bolt,
                title = stringResource(R.string.settings_guidance_level),
                subtitle = (settings["guidance_level"] as? String)?.replaceFirstChar { it.uppercase() } ?: "Full",
                onClick = { /* Show guidance picker */ }
            )

            // Professional Mode
            SettingsGroupHeader(stringResource(R.string.settings_professional_mode))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (profile.professionalModeUnlocked)
                        ForestGreenContainer
                    else
                        StoneGrayLight.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = if (profile.professionalModeUnlocked) ForestGreen else StoneGray
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (profile.professionalModeUnlocked)
                                stringResource(R.string.pro_mode_unlocked)
                            else stringResource(R.string.pro_mode_locked),
                            style = MaterialTheme.typography.bodySmall,
                            color = SoilDark
                        )
                    }
                }
            }

            // Data
            SettingsGroupHeader("Data")

            SettingsClickItem(
                icon = Icons.Filled.Download,
                title = stringResource(R.string.settings_export_progress),
                subtitle = "View your learning summary",
                onClick = { /* Export progress */ }
            )

            SettingsClickItem(
                icon = Icons.Filled.Delete,
                title = stringResource(R.string.settings_reset_progress),
                subtitle = "Clear all progress and start over",
                onClick = { showResetDialog = true },
                titleColor = ErrorRed
            )

            // About
            SettingsGroupHeader("About")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CleanWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Consica Code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoilDark
                    )
                    Text(
                        text = stringResource(R.string.settings_version),
                        style = MaterialTheme.typography.bodySmall,
                        color = StoneGray
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Built with 💚 for learners in Nepal and beyond. Grow your coding ecosystem one seed at a time!",
                        style = MaterialTheme.typography.bodySmall,
                        color = StoneGray
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Progress?") },
            text = {
                Text("This will clear all your XP, badges, completed lessons, and settings. This cannot be undone.")
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { repository.resetAllProgress() }
                    showResetDialog = false
                }) {
                    Text("Reset", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = DialogShape,
            containerColor = CleanWhite
        )
    }
}

@Composable
fun SettingsGroupHeader(title: String) {
    Spacer(Modifier.height(8.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = ForestGreen,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    )
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = ForestGreen)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = SoilDark)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = StoneGray)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun SettingsClickItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = SoilDark
) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = ForestGreen)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = titleColor)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = StoneGray)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = StoneGray)
        }
    }
}