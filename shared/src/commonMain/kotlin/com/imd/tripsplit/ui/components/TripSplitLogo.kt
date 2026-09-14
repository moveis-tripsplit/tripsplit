package com.imd.tripsplit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.GreenTravel

// ==========================================
// ✈️ TripSplit Logo Oficial em Vetor
// ==========================================

fun createTripSplitLogoVector(): ImageVector {
    val loopPath = PathParser().parsePathString(
        "M49.98,19.62c-.32,0-.64,0-.95.02l-15.78,10.85c-1.01,2.26-1.57,4.78-1.57,7.42,0,3.76,1.13,7.25,3.07,10.15-11.76,10.82-24.64,7.82-24.58-3.75-5.33,8.44-3.5,14.4,4.21,17.22,9.62,1.85,16.77-1.87,23.99-9.48,3.16,2.6,7.2,4.16,11.61,4.16,10.11,0,18.3-8.19,18.3-18.3s-8.19-18.3-18.3-18.3ZM50.05,50.94c-7.9,0-13.24-5.12-13.24-13.02s5.5-12.79,13.4-12.79,13.09,5.2,13.09,13.1-5.35,12.72-13.24,12.72Z"
    ).toNodes()

    val planePath = PathParser().parsePathString(
        "M38.59,23.32c-5.04,3.07-14.26,8.01-19.91,11.66-9.94,6.44-13.44,14.95-14.61,17.77-.62,1.49-2.07-8.68,4.86-18.28L.49,28.88c-.58-.38-.65-1.2-.15-1.69l1.33-1.26c.64-.61,1.54-.85,2.4-.65l11.21,2.72s.06-.04.09-.07c5.22-3.96,8.69-6.44,11.54-8.48l-15.08-9.88c-.82-.54-.82-1.74,0-2.28l1.36-.91c.77-.51,1.72-.68,2.61-.47l22.38,5.23C49.82,2.48,58.54-3.11,62.66,1.87c4.41,5.34-15.24,16.06-24.06,21.44Z"
    ).toNodes()

    val dollarPath = PathParser().parsePathString(
        "M49.2,30.5h1.8v1.6c1.8,0.3,3.1,1.7,3.1,3.5h-2.1c0-0.9-0.6-1.6-1.7-1.6s-1.7,0.6-1.7,1.4c0,0.9,0.7,1.2,2.1,1.7,2.1,0.8,3.4,1.7,3.4,3.7,0,1.8-1.3,3.1-3.1,3.4v1.8h-1.8v-1.8c-1.9-0.3-3.3-1.8-3.4-3.7h2.1c0.1,1.1,0.8,1.8,2,1.8,1.1,0,1.9-0.7,1.9-1.6,0-0.9-0.7-1.3-2.1-1.8-2-0.7-3.4-1.6-3.4-3.6,0-1.7,1.3-3.1,2.9-3.4v-1.8z"
    ).toNodes()

    return ImageVector.Builder(
        name = "TripSplitLogo",
        defaultWidth = 68.dp,
        defaultHeight = 62.dp,
        viewportWidth = 68.28f,
        viewportHeight = 62.01f
    ).apply {
        // Moeda e fita verde
        addPath(pathData = loopPath, fill = SolidColor(GreenTravel))
        // Cifrão central
        addPath(pathData = dollarPath, fill = SolidColor(GreenTravel))
        // Avião azul sobrevoando
        addPath(pathData = planePath, fill = SolidColor(BlueOcean))
    }.build()
}

@Composable
fun TripSplitLogo(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp
) {
    val logoVector = remember { createTripSplitLogoVector() }
    Image(
        imageVector = logoVector,
        contentDescription = "Logotipo TripSplit",
        modifier = modifier.size(size)
    )
}
