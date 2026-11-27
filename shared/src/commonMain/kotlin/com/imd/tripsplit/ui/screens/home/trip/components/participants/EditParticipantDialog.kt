package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingSM

@Composable
fun EditParticipantDialog(
    participant: TripParticipant,
    errorMessage: String? = null,
    isCreatorUser: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (TripParticipant) -> Unit,
) {
    var name by remember(participant) { mutableStateOf(participant.name) }
    var role by remember(participant) { mutableStateOf(participant.role) }
    var contact by remember(participant) { mutableStateOf(participant.contactOrPix ?: "") }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Editar Viajante",
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
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        validationError = null
                    },
                    label = { Text("Nome do Viajante *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )

                Text(
                    text = if (participant.isCreator) "Papel na Viagem (Criador):" else "Papel na Viagem:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                ) {
                    FilterChip(
                        selected = !participant.isCreator && role == "Participante",
                        onClick = {
                            if (participant.isCreator) {
                                validationError = "O criador da viagem sempre será o organizador e não pode ter seu papel alterado."
                            } else {
                                role = "Participante"
                                validationError = null
                            }
                        },
                        enabled = !participant.isCreator,
                        label = { Text("Participante", fontSize = 12.sp) },
                        shape = RoundedCornerShape(RadiusMD),
                        colors = FilterChipDefaults.filterChipColors(),
                    )
                    FilterChip(
                        selected = participant.isCreator || role == "Organizador",
                        onClick = {
                            if (!isCreatorUser && !participant.isOrganizer) {
                                validationError = "Apenas o criador da viagem pode definir participantes como organizador."
                            } else {
                                role = "Organizador"
                                validationError = null
                            }
                        },
                        enabled = participant.isCreator || isCreatorUser || participant.isOrganizer,
                        label = { Text(if (participant.isCreator) "Organizador (Criador)" else "Organizador", fontSize = 12.sp) },
                        shape = RoundedCornerShape(RadiusMD),
                        colors = FilterChipDefaults.filterChipColors(),
                    )
                }

                if (participant.isCreator) {
                    Text(
                        text = "ℹ️ O criador da viagem é sempre o organizador.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else if (!isCreatorUser) {
                    Text(
                        text = "ℹ️ Apenas o criador da viagem pode definir participantes como organizador.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Chave PIX ou Contato", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: 11999998888 ou chave pix", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )

                val displayError = validationError ?: errorMessage
                if (displayError != null) {
                    Text(
                        text = displayError,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmedName = name.trim()
                    if (trimmedName.length < 2) {
                        validationError = "O nome deve ter pelo menos 2 caracteres."
                        return@Button
                    }

                    val initials = trimmedName
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .map { it.first().uppercase() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "P" }

                    val finalRole = if (participant.isCreator) "Organizador" else role
                    val isOrganizer = if (participant.isCreator) true else finalRole.equals("Organizador", ignoreCase = true)

                    if (isOrganizer && !participant.isOrganizer && !isCreatorUser) {
                        validationError = "Apenas o criador da viagem pode definir participantes como organizador."
                        return@Button
                    }

                    val updated = participant.copy(
                        name = trimmedName,
                        initials = initials,
                        role = finalRole,
                        isOrganizer = isOrganizer,
                        isCreator = participant.isCreator,
                        contactOrPix = contact.trim().ifEmpty { null },
                    )
                    onConfirm(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(RadiusMD),
            ) {
                Text("Salvar Alterações", color = MaterialTheme.colorScheme.onPrimary)
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
