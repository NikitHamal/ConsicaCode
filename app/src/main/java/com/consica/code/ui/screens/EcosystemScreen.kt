package com.consica.code.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.ui.components.TerraDialogue
import com.consica.code.ui.theme.ForestGreen

@Composable
fun EcosystemScreen(
    onContinue: () -> Unit
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
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(ForestGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.ecosystem_success_description),
                    modifier = Modifier.size(100.dp),
                    tint = ForestGreen
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            TerraDialogue(message = stringResource(R.string.ecosystem_success_message))

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onContinue) {
                Text(stringResource(R.string.ecosystem_continue_button), fontWeight = FontWeight.Bold)
            }
        }
    }
}
