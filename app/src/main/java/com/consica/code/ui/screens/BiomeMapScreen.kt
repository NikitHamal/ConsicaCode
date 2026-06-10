package com.consica.code.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consica.code.R
import com.consica.code.ui.theme.ForestGreen
import com.consica.code.ui.theme.SunYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiomeMapScreen(
    onNavigateToLesson: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.biome_title_forest_floor), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = "XP", tint = SunYellow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.biome_xp_format, 120), fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Simulating a path map
            BiomeNode(title = stringResource(R.string.biome_node_html_basics), isUnlocked = true, onClick = { onNavigateToLesson("html_basics") })
            PathLine()
            BiomeNode(title = stringResource(R.string.biome_node_python_print), isUnlocked = true, onClick = { onNavigateToLesson("python_print") })
            PathLine()
            BiomeNode(title = stringResource(R.string.biome_node_loops_growth), isUnlocked = false, onClick = { })

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun BiomeNode(title: String, isUnlocked: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) ForestGreen else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if(isUnlocked) 8.dp else 0.dp),
        onClick = if (isUnlocked) onClick else { {} }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = title,
                color = if (isUnlocked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PathLine() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = CircleShape
        ) {}
    }
}
