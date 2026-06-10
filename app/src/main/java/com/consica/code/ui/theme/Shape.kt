package com.consica.code.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ConsicaShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// Specific Eco-Logic shapes
val CardShape = RoundedCornerShape(24.dp)
val PillButtonShape = RoundedCornerShape(50)
val DialogShape = RoundedCornerShape(28.dp)
val CodeEditorShape = RoundedCornerShape(16.dp)
val BadgeShape = RoundedCornerShape(20.dp)
val InputShape = RoundedCornerShape(16.dp)
