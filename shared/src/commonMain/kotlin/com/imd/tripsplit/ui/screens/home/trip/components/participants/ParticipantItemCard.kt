package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.components.TripSplitIcons
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM

/**
 * Item individual de participante com padrão de acessibilidade:
 * - Linha completa (fillMaxWidth)
 * - Alvo de toque de 48dp x 48dp nos botões de ação
 * - Rótulos semânticos (contentDescription) detalhados para leitores de tela
 * - O criador da viagem é protegido contra exclusão
 */
@Composable
fun ParticipantItemCard(
    participant: TripParticipant,
    onEditParticipant: ((TripParticipant) -> Unit)?,
    onDeleteParticipant: ((TripParticipant) -> Unit)?,
    onRemoveParticipant: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = false) {},
        shape = RoundedCornerShape(RadiusMD),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpacingMD, vertical = SpacingSM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacingMD),
        ) {
            // Avatar com contraste e iniciais grandes
            ParticipantAvatar(
                initials = participant.initials,
                isOrganizer = participant.isOrganizer,
            )

            // Coluna central com dados do participante
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = participant.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Tags de Papel e Status de Conta
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    ParticipantRoleBadge(
                        role = participant.role,
                        isOrganizer = participant.isOrganizer,
                        isCreator = participant.isCreator,
                    )
                    ParticipantAccountBadge(hasAccount = participant.hasAccount)
                }

                // Chave Pix / Contato
                if (!participant.contactOrPix.isNullOrBlank()) {
                    Text(
                        text = "Pix/Contato: ${participant.contactOrPix}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // E-mail (se cadastrado e diferente do Pix)
                if (!participant.email.isNullOrBlank() && participant.email != participant.contactOrPix) {
                    Text(
                        text = participant.email,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Ações: Botões com tamanho mínimo acessível de 48dp x 48dp e contentDescription semântico
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (onEditParticipant != null) {
                    IconButton(
                        onClick = { onEditParticipant(participant) },
                        modifier =
                            Modifier
                                .size(48.dp)
                                .semantics {
                                    contentDescription = "Editar participante ${participant.name}"
                                },
                    ) {
                        Icon(
                            imageVector = TripSplitIcons.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                // O criador da viagem não pode ser excluído
                if (!participant.isCreator) {
                    IconButton(
                        onClick = {
                            if (onDeleteParticipant != null) {
                                onDeleteParticipant(participant)
                            } else {
                                onRemoveParticipant(participant.id)
                            }
                        },
                        modifier =
                            Modifier
                                .size(48.dp)
                                .semantics {
                                    contentDescription =
                                        if (onDeleteParticipant != null) {
                                            "Excluir participante ${participant.name}"
                                        } else {
                                            "Remover participante ${participant.name}"
                                        }
                                },
                    ) {
                        Icon(
                            imageVector = if (onDeleteParticipant != null) TripSplitIcons.Delete else TripSplitIcons.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}
