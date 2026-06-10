package com.consica.code.ui.ecosystem

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.consica.code.R
import com.consica.code.data.model.AgeGroup
import com.consica.code.data.repository.AppRepository
import com.consica.code.domain.CharacterGuide
import com.consica.code.ui.components.TerraAvatar
import com.consica.code.ui.components.TerraDialogueBubble
import com.consica.code.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcosystemScreen(
    lessonId: String,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: EcosystemViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val earnedXp by viewModel.earnedXp.collectAsState()
    val showCelebration by viewModel.showCelebration.collectAsState()

    val isYoung = profile.ageGroup == AgeGroup.YOUNG

    LaunchedEffect(Unit) {
        viewModel.processResult(lessonId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isSuccess)
                        listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7))
                    else
                        listOf(LeafLight, Color(0xFFFFF9C4), CleanWhite)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.ecosystem_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Spacer(Modifier.weight(0.5f))

            // Central animation area
            Box(
                modifier = Modifier
                    .size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                // Animated sprout / plant graphic
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    if (isSuccess) {
                        // Ground
                        drawOval(
                            color = Color(0xFF795548).copy(alpha = 0.3f),
                            topLeft = Offset(cx - 100f, cy + 40f),
                            size = Size(200f, 40f)
                        )
                        // Stem
                        drawLine(
                            color = Color(0xFF4CAF50),
                            start = Offset(cx, cy + 50f),
                            end = Offset(cx, cy - 30f),
                            strokeWidth = 8f
                        )
                        // Leaves
                        val leafPath = Path().apply {
                            moveTo(cx, cy + 10f)
                            quadraticBezierTo(cx + 40f, cy - 10f, cx + 20f, cy - 30f)
                            quadraticBezierTo(cx + 5f, cy - 15f, cx, cy + 10f)
                        }
                        drawPath(leafPath, color = Color(0xFF66BB6A), style = Fill)
                        val leafPath2 = Path().apply {
                            moveTo(cx, cy + 10f)
                            quadraticBezierTo(cx - 40f, cy - 10f, cx - 20f, cy - 30f)
                            quadraticBezierTo(cx - 5f, cy - 15f, cx, cy + 10f)
                        }
                        drawPath(leafPath2, color = Color(0xFF81C784), style = Fill)
                        // Sprout head
                        drawCircle(
                            color = Color(0xFFFFD54F),
                            radius = 16f,
                            center = Offset(cx, cy - 30f)
                        )
                    } else {
                        // Wilting plant for error
                        drawOval(
                            color = Color(0xFF795548).copy(alpha = 0.3f),
                            topLeft = Offset(cx - 80f, cy + 40f),
                            size = Size(160f, 30f)
                        )
                        drawLine(
                            color = Color(0xFFA5D6A7),
                            start = Offset(cx, cy + 50f),
                            end = Offset(cx + 15f, cy - 10f),
                            strokeWidth = 6f
                        )
                        drawCircle(
                            color = Color(0xFFEF9A9A),
                            radius = 12f,
                            center = Offset(cx + 15f, cy - 15f)
                        )
                    }
                }

                // Animated celebration effects
                if (showCelebration) {
                    repeat(12) { index ->
                        val angle = index * 30f
                        Text(
                            text = "✨",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .offset(
                                    x = (60 * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp,
                                    y = (60 * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Result message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CleanWhite)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSuccess) "🌿" else "💧",
                        fontSize = 48.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (isSuccess)
                            stringResource(R.string.ecosystem_success)
                        else
                            stringResource(R.string.ecosystem_error),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SoilDark,
                        textAlign = TextAlign.Center
                    )
                    if (isSuccess && earnedXp > 0) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.rewards_xp_earned, earnedXp),
                            style = MaterialTheme.typography.headlineSmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Terra feedback
            TerraDialogueBubble(
                message = if (isSuccess) {
                    if (isYoung) "AMAZING! 🌱 Your seed sprouted! Look at that beautiful plant! You're a natural coder!"
                    else "Code executed successfully. Your ecosystem is growing. Continue building your skills."
                } else {
                    if (isYoung) "Don't worry, little explorer! 🦉 Every plant needs a little more water. Let's try again together!"
                    else "There was an issue with the code. Review the output and try again."
                },
                expression = if (isSuccess) "celebrating" else "encouraging",
                showAvatar = isYoung
            )

            Spacer(Modifier.weight(1f))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    shape = PillButtonShape,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
                ) {
                    Text(if (!isSuccess) "Try Again" else "Review Code")
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier.weight(1f),
                    shape = PillButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Continue", color = TextOnPrimary)
                }
            }
        }
    }
}
