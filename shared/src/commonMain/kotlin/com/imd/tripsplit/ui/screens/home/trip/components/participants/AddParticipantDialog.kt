package com.imd.tripsplit.ui.screens.home.trip.components.participants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingSM
import kotlin.random.Random

@Composable
fun AddParticipantDialog(
    isCreator: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (TripParticipant) -> Unit,
) {
    var hasAccountTab by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Participante") }
    var contact by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Viajante",
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
                // Abas de tipo de participante
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(RadiusMD))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(
                                    if (!hasAccountTab) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    hasAccountTab = false
                                    errorMessage = null
                                }
                                .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Sem Conta",
                            fontSize = 12.sp,
                            fontWeight = if (!hasAccountTab) FontWeight.Bold else FontWeight.Normal,
                            color = if (!hasAccountTab) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(
                                    if (hasAccountTab) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    hasAccountTab = true
                                    errorMessage = null
                                }
                                .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Com Conta",
                            fontSize = 12.sp,
                            fontWeight = if (hasAccountTab) FontWeight.Bold else FontWeight.Normal,
                            color = if (hasAccountTab) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                if (hasAccountTab) {
                    // Campo E-mail para vincular conta existente
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                            // Busca automática se já existir
                            val foundUser = TripRepository.findUserByEmail(it)
                            if (foundUser != null && name.isBlank()) {
                                name = foundUser.name
                            }
                        },
                        label = { Text("E-mail da conta *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        placeholder = {
                            Text("ex: amigo@email.com", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                        },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(RadiusMD),
                    )
                }

                // Campo Nome
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text(if (hasAccountTab) "Nome (opcional)" else "Nome do Viajante *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = {
                        Text("Ex: Mariana Silva", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )

                // Seleção de Papel (Organizador vs Participante)
                Text(
                    text = "Papel na Viagem:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                ) {
                    FilterChip(
                        selected = role == "Participante",
                        onClick = {
                            role = "Participante"
                            errorMessage = null
                        },
                        label = { Text("Participante", fontSize = 12.sp) },
                        shape = RoundedCornerShape(RadiusMD),
                        colors = FilterChipDefaults.filterChipColors(),
                    )
                    FilterChip(
                        selected = role == "Organizador",
                        onClick = {
                            if (!isCreator) {
                                errorMessage = "Apenas o criador da viagem pode definir participantes como organizador."
                            } else {
                                role = "Organizador"
                                errorMessage = null
                            }
                        },
                        enabled = isCreator,
                        label = { Text("Organizador", fontSize = 12.sp) },
                        shape = RoundedCornerShape(RadiusMD),
                        colors = FilterChipDefaults.filterChipColors(),
                    )
                }

                if (!isCreator) {
                    Text(
                        text = "ℹ️ Apenas o criador da viagem pode definir participantes como organizador.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                // Campo Contato ou PIX
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Chave PIX ou Contato (opcional)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: 11999998888 ou chave pix", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
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
                    val trimmedEmail = email.trim()

                    if (hasAccountTab) {
                        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
                            errorMessage = "Informe um e-mail válido para vincular a conta."
                            return@Button
                        }
                    } else {
                        if (trimmedName.length < 2) {
                            errorMessage = "O nome deve ter pelo menos 2 caracteres."
                            return@Button
                        }
                    }

                    if (role == "Organizador" && !isCreator) {
                        errorMessage = "Apenas o criador da viagem pode definir participantes como organizador."
                        return@Button
                    }

                    val resolvedName =
                        if (trimmedName.isNotBlank()) {
                            trimmedName
                        } else {
                            val user = TripRepository.findUserByEmail(trimmedEmail)
                            user?.name ?: trimmedEmail.substringBefore("@")
                        }

                    val initials =
                        resolvedName
                            .split(" ")
                            .filter { it.isNotBlank() }
                            .map { it.first().uppercase() }
                            .take(2)
                            .joinToString("")
                            .ifEmpty { "P" }

                    val matchedUser = if (hasAccountTab) TripRepository.findUserByEmail(trimmedEmail) else null

                    val participant =
                        TripParticipant(
                            id = "part-${Random.nextInt(1000, 9999)}",
                            name = resolvedName,
                            initials = initials,
                            role = role,
                            isOrganizer = role.equals("Organizador", ignoreCase = true),
                            hasAccount = hasAccountTab,
                            contactOrPix = contact.trim().ifEmpty { null },
                            email = if (hasAccountTab) trimmedEmail else null,
                            userId = matchedUser?.id,
                        )
                    onConfirm(participant)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(RadiusMD),
            ) {
                Text("Adicionar", color = MaterialTheme.colorScheme.onPrimary)
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
