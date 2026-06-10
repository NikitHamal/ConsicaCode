package com.consica.code.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.ui.components.TerraDialogue

@Composable
fun TerraTutorialScreen(
    lessonId: String,
    onStartCoding: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TerraDialogue(message = stringResource(R.string.terra_tutorial_message))

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onStartCoding,
                modifier = Modifier.fillMaxWidth(0.8f).height(56.dp)
            ) {
                Text(stringResource(R.string.terra_tutorial_start_coding), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
