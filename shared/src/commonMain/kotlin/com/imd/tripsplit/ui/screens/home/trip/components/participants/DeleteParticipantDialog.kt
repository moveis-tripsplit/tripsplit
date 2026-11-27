package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingSM

@Composable
fun DeleteParticipantDialog(
    participant: TripParticipant,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val effectiveError =
        if (participant.isCreator) {
            "O criador da viagem não pode ser excluído da viagem."
        } else {
            errorMessage
        }
    val isBlocked = effectiveError != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Excluir Viajante",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SpacingSM),
            ) {
                Text(
                    text = "Tem certeza que deseja remover ${participant.name} desta viagem?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (isBlocked) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(RadiusMD))
                                .padding(SpacingSM),
                    ) {
                        Text(
                            text = effectiveError.orEmpty(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isBlocked,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f),
                    ),
                shape = RoundedCornerShape(RadiusMD),
            ) {
                Text("Confirmar Exclusão", color = MaterialTheme.colorScheme.onError)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
            }
        },
        shape = RoundedCornerShape(RadiusLG),
        containerColor = MaterialTheme.colorScheme.surface,
    )
}
