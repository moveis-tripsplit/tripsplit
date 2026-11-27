package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.home.trip.TripActivity
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingSM
import kotlin.random.Random
import com.imd.tripsplit.domain.activityDateError
import com.imd.tripsplit.domain.tripDateRange
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AddActivityDialog(
    availableParticipants: List<TripParticipant>,
    startDate: String,
    endDate: String,
    initialActivity: TripActivity? = null,
    onDismiss: () -> Unit,
    onConfirm: (TripActivity) -> Unit,
) {
    var title by remember(initialActivity?.id) {
        mutableStateOf(initialActivity?.title.orEmpty())
    }

    val originalDateHour = initialActivity?.dateHour.orEmpty()
    val originalDigits = originalDateHour.filter { it in '0'..'9' }

    val needsDateReentry = originalDateHour.isNotBlank() && originalDigits.length != 12

    var dateHourDigits by remember(initialActivity?.id) {
        mutableStateOf(if (needsDateReentry) "" else originalDigits)
    }

    var dateFieldFocused by remember(initialActivity?.id) { mutableStateOf(false) }
    var dateFieldEdited by remember(initialActivity?.id) { mutableStateOf(false) }

    val dateHour = formatActivityDateHour(dateHourDigits)

    var location by remember(initialActivity?.id) {
        mutableStateOf(initialActivity?.location.orEmpty())
    }

    var costText by remember(initialActivity?.id) {
        mutableStateOf(initialActivity?.estimatedCost?.toString().orEmpty())
    }

    var selectedParticipantIds by remember(initialActivity?.id) {
        mutableStateOf(
            initialActivity?.participantIds.orEmpty().toSet(),
        )
    }

    val isEditing = initialActivity != null

    val parsedCost = costText.trim().replace(",", ".").toDoubleOrNull()

    val isCostValid =
        costText.isBlank() ||
                (
                        parsedCost != null &&
                                parsedCost.isFinite() &&
                                parsedCost >= 0.0
                        )

    val tripPeriod = tripDateRange(startDate, endDate)
    val dateError = activityDateError(dateHour, tripPeriod)
    val showDateError =
        tripPeriod != null && dateFieldEdited && !dateFieldFocused && dateError != null
    val canConfirm =
        title.isNotBlank() &&
                location.isNotBlank() &&
                dateError == null &&
                isCostValid


    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) "Editar atividade" else "Adicionar atividade",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
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
                    value = dateHourDigits,
                    onValueChange = { input ->
                        val digits = input.filter { it in '0'..'9' }
                        if (digits.length <= 12 && digits != dateHourDigits) {
                            dateHourDigits = digits
                            dateFieldEdited = true
                        }
                    },
                    label = { Text("Data / Horário *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("DD/MM/AAAA • HH:mm", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    visualTransformation = ActivityDateHourMask,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = showDateError,
                    supportingText = {
                        val message = when {
                            tripPeriod == null ->
                                "Defina um período válido no formulário da viagem para adicionar atividades."

                            showDateError -> dateError.orEmpty()

                            needsDateReentry && !dateFieldEdited ->
                                "Data salva: $originalDateHour. Informe a data completa com o ano."

                            else -> "Digite apenas os números da data e do horário."
                        }
                        Text(message)
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { dateFieldFocused = it.isFocused },
                    shape = RoundedCornerShape(RadiusMD),
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Local *", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    placeholder = { Text("Ex: Ilha do Campeche", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface,
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
                    isError = !isCostValid,
                    supportingText = {
                        if (!isCostValid) {
                            Text("Informe um valor válido, igual ou maior que zero.")
                        }
                    },
                )
                Text(
                    text = "Participantes da atividade",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                if (availableParticipants.isEmpty()) {
                    Text(
                        text = "Adicione participantes à viagem para selecioná-los aqui.",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    availableParticipants.forEach { participant ->
                        val isSelected = participant.id in selectedParticipantIds

                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .toggleable(
                                        value = isSelected,
                                        role = Role.Checkbox,
                                        onValueChange = { checked ->
                                            selectedParticipantIds =
                                                if (checked) {
                                                    selectedParticipantIds + participant.id
                                                } else {
                                                    selectedParticipantIds - participant.id
                                                }
                                        },
                                    )
                                    .padding(horizontal = SpacingSM),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null,
                            )

                            Text(
                                text = participant.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (canConfirm) {
                        val participantIds =
                            availableParticipants
                                .map { it.id }
                                .filter { it in selectedParticipantIds }
                                .distinct()

                        val activity =
                            if (initialActivity != null) {
                                initialActivity.copy(
                                    title = title.trim(),
                                    dateHour = dateHour.trim(),
                                    location = location.trim(),
                                    estimatedCost = parsedCost,
                                    participantIds = participantIds,
                                )
                            } else {
                                TripActivity(
                                    id = Random.nextInt(1000, 9999).toString(),
                                    title = title.trim(),
                                    dateHour = dateHour.trim(),
                                    location = location.trim(),
                                    estimatedCost = parsedCost,
                                    participantIds = participantIds,
                                )
                            }

                        onConfirm(activity)

                    }
                },
                enabled = canConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(RadiusMD),
            ) {
                Text(
                    text = if (isEditing) "Salvar alterações" else "Adicionar",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
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

private fun formatActivityDateHour(digits: String): String = buildString {
    digits.forEachIndexed { index, digit ->
        when (index) {
            2, 4 -> append('/')
            8 -> append(" • ")
            10 -> append(':')
        }
        append(digit)
    }
}

private object ActivityDateHourMask : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = formatActivityDateHour(text.text)

        val offsets = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return 0

                var digitsSeen = 0
                for (index in formatted.indices) {
                    if (formatted[index] in '0'..'9') digitsSeen++
                    if (digitsSeen == offset) return index + 1
                }
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int =
                formatted.take(offset).count { it in '0'..'9' }
        }

        return TransformedText(AnnotatedString(formatted), offsets)
    }
}
