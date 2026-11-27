package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.theme.RadiusXL

/**
 * Badge de papel do participante (Organizador, Criador ou Participante).
 */
@Composable
fun ParticipantRoleBadge(
    role: String,
    isOrganizer: Boolean,
    isCreator: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val badgeText = if (isCreator) "Organizador (Criador)" else role
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(RadiusXL))
                .background(
                    if (isOrganizer) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                )
                .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = badgeText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color =
                if (isOrganizer) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
        )
    }
}

/**
 * Badge indicando se o participante possui conta vinculada ou é convidado.
 */
@Composable
fun ParticipantAccountBadge(
    hasAccount: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!hasAccount) {
        Box(
            modifier =
                modifier
                    .clip(RoundedCornerShape(RadiusXL))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusXL))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
        ) {
            Text(
                text = "Convidado (sem conta)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        Box(
            modifier =
                modifier
                    .clip(RoundedCornerShape(RadiusXL))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(RadiusXL))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
        ) {
            Text(
                text = "Com conta",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
