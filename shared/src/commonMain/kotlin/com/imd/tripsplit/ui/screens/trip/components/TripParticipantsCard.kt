package com.imd.tripsplit.ui.screens.trip.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS

@Composable
fun TripParticipantsCard(
    participants: List<TripParticipant>,
    onAddParticipantClick: () -> Unit,
    onRemoveParticipant: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(RadiusLG),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(SpacingLG)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Participantes (${participants.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Box(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(RadiusXL))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusXL))
                            .padding(horizontal = SpacingSM, vertical = 2.dp),
                ) {
                    Text(
                        text = "Pode adicionar depois",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "Quem vai dividir os custos da viagem com você:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(SpacingSM))

            if (participants.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(RadiusMD))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(RadiusMD))
                            .padding(SpacingMD),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhum participante adicionado ainda",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Adicione ou convide amigos para dividir as despesas.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            } else {
                participants.chunked(2).forEach { rowParticipants ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                    ) {
                        rowParticipants.forEach { participant ->
                            Box(
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(RadiusMD))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusMD))
                                        .padding(SpacingSM),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(if (participant.isOrganizer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = participant.initials,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (participant.isOrganizer) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = participant.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            text = participant.role,
                                            fontSize = 9.sp,
                                            color = if (participant.isOrganizer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                        )
                                    }

                                    IconButton(
                                        onClick = { onRemoveParticipant(participant.id) },
                                        modifier = Modifier.size(20.dp),
                                    ) {
                                        Text("✕", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                        if (rowParticipants.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(SpacingSM))

            Button(
                onClick = onAddParticipantClick,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(RadiusMD),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("+ Convidar ou adicionar participantes", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "ℹ️ Amigos sem o app instalado também podem ser adicionados.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}
