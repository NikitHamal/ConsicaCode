package com.consica.code.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerraAvatar(
    expression: String = "happy",
    size: Dp = 64.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (expression == "celebrating" || expression == "excited") 8f else 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .offset(y = (-bounce).dp)
            .size(size)
            .clip(CircleShape)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val center = Offset(canvasWidth / 2, canvasHeight / 2)
            val radius = minOf(canvasWidth, canvasHeight) / 2

            // Owl body
            drawCircle(
                color = Color(0xFF8B6914),
                radius = radius * 0.85f,
                center = Offset(center.x, center.y + radius * 0.08f)
            )

            // Belly
            drawCircle(
                color = Color(0xFFF5DEB3),
                radius = radius * 0.5f,
                center = Offset(center.x, center.y + radius * 0.15f)
            )

            // Left eye white
            drawCircle(
                color = Color.White,
                radius = radius * 0.28f,
                center = Offset(center.x - radius * 0.28f, center.y - radius * 0.1f)
            )
            // Right eye white
            drawCircle(
                color = Color.White,
                radius = radius * 0.28f,
                center = Offset(center.x + radius * 0.28f, center.y - radius * 0.1f)
            )

            // Eye pupils
            val eyeColor = when (expression) {
                "happy", "excited", "celebrating" -> Color(0xFF2D5A27)
                "proud" -> Color(0xFF4DA8DA)
                "thinking", "confused" -> Color(0xFF8D6E63)
                "sleepy" -> Color(0xFF78909C)
                else -> Color(0xFF2D5A27)
            }
            drawCircle(
                color = eyeColor,
                radius = radius * 0.14f,
                center = Offset(center.x - radius * 0.28f, center.y - radius * 0.1f)
            )
            drawCircle(
                color = eyeColor,
                radius = radius * 0.14f,
                center = Offset(center.x + radius * 0.28f, center.y - radius * 0.1f)
            )

            // Beak
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(center.x - radius * 0.1f, center.y + radius * 0.05f)
                lineTo(center.x, center.y + radius * 0.2f)
                lineTo(center.x + radius * 0.1f, center.y + radius * 0.05f)
                close()
            }
            drawPath(
                path = path,
                color = Color(0xFFFF8F00)
            )
        }

        // Expression text
        Text(
            text = when (expression) {
                "happy" -> "🦉"
                "excited" -> "✨"
                "thinking" -> "🤔"
                "confused" -> "❓"
                "proud" -> "🌟"
                "sleepy" -> "😴"
                "focused" -> "🎯"
                "professional" -> "📚"
                "encouraging" -> "💚"
                "celebrating" -> "🎉"
                "concerned" -> "💧"
                else -> "🦉"
            },
            fontSize = (size.value / 3.5).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TerraDialogueBubble(
    message: String,
    expression: String = "happy",
    isVisible: Boolean = true,
    showAvatar: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 })
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (showAvatar) {
                TerraAvatar(
                    expression = expression,
                    size = 48.dp
                )
                Spacer(Modifier.width(10.dp))
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun BiomeNode(
    title: String,
    isUnlocked: Boolean = true,
    isActive: Boolean = false,
    isCompleted: Boolean = false,
    emoji: String = "🌿",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .scale(pulse)
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = when {
                    !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    isCompleted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    isActive -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (isUnlocked) emoji else "🔒",
                fontSize = 28.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
            if (isCompleted) {
                Text("✓", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
            }
        }
    }
}