
package com.imd.tripsplit.ui.screens.home.trip.despesas.componentsDespesas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.SurfaceLight
import com.imd.tripsplit.ui.theme.TextPrimary
import com.imd.tripsplit.ui.theme.TextSecondary

@Composable
fun ModalCadastroDespesa(
    participantes: List<Participant>,
    onFechar: () -> Unit,
    onSalvar: (String, Double, String, List<String>) -> Unit
) {
    var descricao by remember { mutableStateOf("") }
    var valorTexto by remember { mutableStateOf("") }
    var pagadorId by remember { mutableStateOf("") }
    var participantesSelecionados by remember { mutableStateOf(setOf<String>()) }

    val valor = valorTexto.replace(",", ".").toDoubleOrNull()

    val podeSalvar =
        descricao.isNotBlank() &&
            valor != null &&
            valor > 0 &&
            pagadorId.isNotBlank() &&
            participantesSelecionados.isNotEmpty()

    val coresCampos =
        OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = BlueOcean,
            unfocusedBorderColor = BorderLight,
            focusedLabelColor = BlueOcean,
            unfocusedLabelColor = TextSecondary,
            cursorColor = BlueOcean,
            focusedContainerColor = SurfaceLight,
            unfocusedContainerColor = SurfaceLight
        )

    AlertDialog(
        onDismissRequest = onFechar,
        containerColor = SurfaceLight,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary,
        title = {
            Text(
                text = "Cadastrar despesa",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = coresCampos
                )

                OutlinedTextField(
                    value = valorTexto,
                    onValueChange = { valorTexto = it },
                    label = { Text("Valor total (R$)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = coresCampos
                )

                // Seleção do pagador
                Text(
                    text = "Quem pagou?",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    participantes.forEach { participante ->
                        ParticipanteBolinha(
                            participante = participante,
                            selecionado = pagadorId == participante.id,
                            onClick = {
                                pagadorId = participante.id
                            }
                        )
                    }
                }

                // Seleção de participantes da divisão
                Text(
                    text = "Quem vai dividir a despesa?",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Você pode selecionar mais de uma pessoa.",
                    color = TextSecondary
                )

                Column(
                    modifier = Modifier.heightIn(max = 180.dp)
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        participantes.forEach { participante ->
                            ParticipanteBolinha(
                                participante = participante,
                                selecionado = participante.id in participantesSelecionados,
                                onClick = {
                                    participantesSelecionados =
                                        if (participante.id in participantesSelecionados) {
                                            participantesSelecionados - participante.id
                                        } else {
                                            participantesSelecionados + participante.id
                                        }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSalvar(
                        descricao.trim(),
                        valor!!,
                        pagadorId,
                        participantesSelecionados.toList()
                    )
                },
                enabled = podeSalvar,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BlueOcean,
                        contentColor = Color.White
                    )
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onFechar) {
                Text(
                    text = "Cancelar",
                    color = TextSecondary
                )
            }
        }
    )
}

@Composable
private fun ParticipanteBolinha(
    participante: Participant,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val iniciais =
        participante.name
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            Modifier
                .widthIn(min = 52.dp)
                .clickable(onClick = onClick)
                .padding(vertical = 4.dp)
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selecionado) BlueOcean else SurfaceLight
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (selecionado) BlueOcean else BorderLight,
                        shape = CircleShape
                    ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (selecionado) "✓" else iniciais,
                color = if (selecionado) Color.White else TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = participante.name,
            color = if (selecionado) BlueOcean else TextSecondary,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
