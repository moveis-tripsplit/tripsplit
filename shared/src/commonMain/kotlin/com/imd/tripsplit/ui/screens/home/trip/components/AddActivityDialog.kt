package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.imd.tripsplit.ui.screens.home.trip.TripActivity
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingSM
import kotlin.random.Random

@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onConfirm: (TripActivity) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var dateHour by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var costText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Atividade",
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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da atividade *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: Passeio de Barco", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle =
                        LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )
                OutlinedTextField(
                    value = dateHour,
                    onValueChange = { dateHour = it },
                    label = { Text("Data / Horário", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: 16/11 • 10:00", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle =
                        LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Local", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: Ilha do Campeche", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle =
                        LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )
                OutlinedTextField(
                    value = costText,
                    onValueChange = { costText = it },
                    label = { Text("Custo estimado (R$)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: 120.00", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle =
                        LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(RadiusMD),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val cost = costText.replace(",", ".").toDoubleOrNull()
                        val activity =
                            TripActivity(
                                id = Random.nextInt(1000, 9999).toString(),
                                title = title.trim(),
                                dateHour = dateHour.trim().ifEmpty { "A definir" },
                                location = location.trim().ifEmpty { "A definir" },
                                estimatedCost = cost,
                            )
                        onConfirm(activity)
                    }
                },
                enabled = title.isNotBlank(),
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
