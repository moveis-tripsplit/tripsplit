package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Avatar circular com iniciais legíveis para o participante da viagem.
 */
@Composable
fun ParticipantAvatar(
    initials: String,
    isOrganizer: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    if (isOrganizer) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color =
                if (isOrganizer) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
        )
    }
}
