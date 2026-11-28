package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.domain.tripDateRange
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingSM

@Composable
fun TripDatesCard(
    startDate: String,
    endDate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(RadiusLG),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(SpacingLG)) {
            Text(
                text = "Período da viagem *",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(SpacingSM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingSM),
            ) {
                TripDateField(
                    value = startDate,
                    onValueChange = onStartDateChange,
                    label = "Data de Início",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "→",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                )

                TripDateField(
                    value = endDate,
                    onValueChange = onEndDateChange,
                    label = "Data de Término",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(SpacingSM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (tripDateRange(startDate, endDate) != null) {
                    Box(
                        modifier =
                            Modifier
                                .clip(RoundedCornerShape(RadiusXL))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = SpacingSM, vertical = 2.dp),
                    ) {
                        Text(
                            text = "✓ Período definido",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                } else if (startDate.isNotBlank() && endDate.isNotBlank()) {
                    Text(
                        text = "Use DD/MM/AAAA. O término deve ser igual ou posterior ao início.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error,
                    )
                } else {
                    Text(
                        text = "💡 Datas flexíveis podem ser alteradas depois",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

private fun formatTripDate(digits: String): String =
    buildString {
        digits.forEachIndexed { index, digit ->
            if (index == 2 || index == 4) append('/')
            append(digit)
        }
    }

private object TripDateMask : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = formatTripDate(text.text)

        val offsets =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    var position = offset
                    if (offset > 2) position++
                    if (offset > 4) position++
                    return position
                }

                override fun transformedToOriginal(offset: Int): Int = formatted.take(offset).count { it in '0'..'9' }
            }

        return TransformedText(AnnotatedString(formatted), offsets)
    }
}

@Composable
private fun TripDateField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val digits = value.filter { it in '0'..'9' }

    OutlinedTextField(
        value = digits,
        onValueChange = { input ->
            val newDigits = input.filter { it in '0'..'9' }
            if (newDigits.length <= 8) {
                onValueChange(formatTripDate(newDigits))
            }
        },
        readOnly = !enabled,
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        placeholder = {
            Text(
                text = "DD/MM/AAAA",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        visualTransformation = TripDateMask,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle =
            LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
        modifier = modifier,
        shape = RoundedCornerShape(RadiusMD),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
        singleLine = true,
    )
}
