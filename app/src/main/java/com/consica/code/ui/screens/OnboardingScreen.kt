package com.consica.code.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.ui.components.TerraDialogue

@Composable
fun OnboardingScreen(
    onComplete: (String) -> Unit
) {
    var selectedAgeGroup by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TerraDialogue(message = stringResource(R.string.onboarding_title_age))

            Spacer(modifier = Modifier.height(32.dp))

            AgeOptionCard(
                text = stringResource(R.string.onboarding_age_8_12),
                selected = selectedAgeGroup == "8-12",
                onClick = { selectedAgeGroup = "8-12" }
            )

            AgeOptionCard(
                text = stringResource(R.string.onboarding_age_13_16),
                selected = selectedAgeGroup == "13-16",
                onClick = { selectedAgeGroup = "13-16" }
            )

            AgeOptionCard(
                text = stringResource(R.string.onboarding_age_16_plus),
                selected = selectedAgeGroup == "16+",
                onClick = { selectedAgeGroup = "16+" }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { selectedAgeGroup?.let { onComplete(it) } },
                enabled = selectedAgeGroup != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (selectedAgeGroup == "16+")
                           stringResource(R.string.onboarding_start_pro)
                           else stringResource(R.string.onboarding_start),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AgeOptionCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 8.dp else 2.dp
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
