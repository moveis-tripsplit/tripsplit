package com.imd.tripsplit.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ==========================================
// 📐 TripSplit Design System - Raios de Borda
// ==========================================

val RadiusSM = 8.dp   // Badges, tags, botões pequenos
val RadiusMD = 12.dp  // Campos de texto (inputs), botões padrão
val RadiusLG = 16.dp  // Cards pequenos e modais
val RadiusXL = 24.dp  // Cards de destaque, containers de formulário

val TripSplitShapes = Shapes(
    small = RoundedCornerShape(RadiusSM),
    medium = RoundedCornerShape(RadiusMD),
    large = RoundedCornerShape(RadiusLG),
    extraLarge = RoundedCornerShape(RadiusXL)
)
