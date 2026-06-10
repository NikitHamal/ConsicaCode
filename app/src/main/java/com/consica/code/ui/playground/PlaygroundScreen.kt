package com.consica.code.ui.playground

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.consica.code.R
import com.consica.code.data.model.*
import com.consica.code.domain.CharacterGuide
import com.consica.code.ui.components.TerraDialogueBubble
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(
    lessonId: String,
    onRunComplete: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: PlaygroundViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val codeState by viewModel.codeFieldValue.collectAsState()
    val output by viewModel.output.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val showOutput by viewModel.showOutput.collectAsState()
    val language by viewModel.currentLanguage.collectAsState()
    val showHint by viewModel.showHint.collectAsState()

    val isYoung = profile.ageGroup == AgeGroup.YOUNG
    val isPro = profile.ageGroup == AgeGroup.YOUNG_ADULT || profile.professionalModeUnlocked

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.playground_title),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                // Language selector
                FilterChip(
                    selected = language == CodeLanguage.HTML,
                    onClick = { viewModel.setLanguage(CodeLanguage.HTML) },
                    label = { Text("HTML", fontSize = 11.sp) },
                    modifier = Modifier.padding(end = 2.dp)
                )
                FilterChip(
                    selected = language == CodeLanguage.PYTHON,
                    onClick = { viewModel.setLanguage(CodeLanguage.PYTHON) },
                    label = { Text("Python", fontSize = 11.sp) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(onClick = { viewModel.toggleHint() }) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = stringResource(R.string.playground_hints))
                }
                IconButton(onClick = { /* Save */ }) {
                    Icon(Icons.Filled.Save, contentDescription = stringResource(R.string.playground_save))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Hint bar
        AnimatedVisibility(visible = showHint) {
            TerraDialogueBubble(
                message = if (language == CodeLanguage.HTML)
                    "Try: <h1>Your title here</h1> or <p>Your paragraph</p>"
                else
                    "Try: print('Hello, world!')",
                expression = "thinking",
                showAvatar = isYoung,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Code Editor
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp),
            shape = CodeEditorShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Editor header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2D2D2D))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (language) {
                            CodeLanguage.HTML -> "index.html"
                            CodeLanguage.PYTHON -> "main.py"
                            CodeLanguage.CSS -> "style.css"
                        },
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(Modifier.weight(1f))
                    if (isPro) {
                        Text(
                            text = "Ln ${codeState.text.lines().size}",
                            color = Color(0xFF888888),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Code input area
                BasicTextField(
                    value = codeState,
                    onValueChange = { viewModel.updateCode(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        color = Color(0xFFE0E0E0),
                        fontFamily = FontFamily.Monospace,
                        fontSize = if (isPro) 14.sp else 16.sp,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = SolidColor(Color(0xFF4DA8DA)),
                    decorationBox = { innerTextField ->
                        if (codeState.text.isEmpty()) {
                            Text(
                                text = "Write your code here...",
                                color = Color(0xFF666666),
                                fontFamily = FontFamily.Monospace,
                                fontSize = if (isPro) 14.sp else 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        // Run button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.runCode(
                        onSuccess = { lessonId -> onRunComplete(lessonId) }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isYoung) 60.dp else 52.dp),
                shape = PillButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isYoung) MossGreen else ForestGreen
                ),
                enabled = !isRunning
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextOnPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        if (isYoung) Icons.Filled.Park else Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.a11y_run_code),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isYoung) stringResource(R.string.playground_grow)
                    else stringResource(R.string.playground_run),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextOnPrimary
                )
            }
        }

        // Output panel
        AnimatedVisibility(
            visible = showOutput,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .heightIn(max = 200.dp),
                shape = CodeEditorShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.playground_output),
                            color = Color(0xFF888888),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = { viewModel.clearOutput() },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFF888888),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = output.ifEmpty { "Output will appear here..." },
                        color = if (output.contains("Error", ignoreCase = true))
                            Color(0xFFEF5350)
                        else Color(0xFFA5D6A7),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}