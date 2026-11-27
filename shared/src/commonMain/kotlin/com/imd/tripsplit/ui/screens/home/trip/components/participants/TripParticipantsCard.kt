package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS

/**
 * Card principal modularizado e acessível de participantes da viagem.
 */
@Composable
fun TripParticipantsCard(
    participants: List<TripParticipant>,
    onAddParticipantClick: () -> Unit,
    onRemoveParticipant: (String) -> Unit = {},
    onEditParticipant: ((TripParticipant) -> Unit)? = null,
    onDeleteParticipant: ((TripParticipant) -> Unit)? = null,
    isOrganizer: Boolean = false,
    isCreator: Boolean = false,
    currentUserId: String? = null,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(RadiusLG),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(SpacingLG)) {
            TripParticipantsHeader(count = participants.size)

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(SpacingSM))
                TripParticipantsErrorBanner(errorMessage = errorMessage)
            }

            Spacer(modifier = Modifier.height(SpacingMD))

            if (participants.isEmpty()) {
                ParticipantEmptyState()
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SpacingSM),
                ) {
                    participants.forEach { participant ->
                        ParticipantItemCard(
                            participant = participant,
                            onEditParticipant = onEditParticipant,
                            onDeleteParticipant = onDeleteParticipant,
                            onRemoveParticipant = onRemoveParticipant,
                            isOrganizer = isOrganizer,
                            isCreator = isCreator,
                            currentUserId = currentUserId,
                        )
                    }
                }
            }

            if (isCreator) {
                Spacer(modifier = Modifier.height(SpacingMD))
                AddParticipantButton(onClick = onAddParticipantClick)
            }

            Spacer(modifier = Modifier.height(SpacingSM))

            ParticipantFooterNotice()
        }
    }
}

@Composable
private fun TripParticipantsHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Participantes ($count)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(RadiusXL))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusXL))
                    .padding(horizontal = SpacingSM, vertical = 4.dp),
        ) {
            Text(
                text = "Pode adicionar depois",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }

    Spacer(modifier = Modifier.height(SpacingXS))

    Text(
        text = "Quem vai dividir os custos da viagem com você:",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun TripParticipantsErrorBanner(errorMessage: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(RadiusMD))
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(SpacingSM),
    ) {
        Text(
            text = errorMessage,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

@Composable
private fun AddParticipantButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(RadiusMD),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(48.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = TripSplitIcons.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.size(SpacingSM))
            Text(
                text = "Convidar ou adicionar participantes",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun ParticipantFooterNotice() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpacingXS),
    ) {
        Icon(
            imageVector = TripSplitIcons.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = "Amigos sem o app instalado também podem ser adicionados.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
