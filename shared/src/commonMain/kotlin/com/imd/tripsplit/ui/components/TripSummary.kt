package com.imd.tripsplit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.ui.theme.GreenTravel
import com.imd.tripsplit.ui.theme.TextSecondary

@Composable
fun TripSummary(
    trip: Trip,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Cabeçalho do Resumo
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    StatusChip(trip.status)
                    Text(
                        text = trip.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${trip.destination} • ${trip.periodLabel()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                // Estatísticas e Resumo Financeiro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    StatItem(
                        label = "Gasto Total",
                        value = trip.totalAmount.toBrl(),
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Seu Saldo Final",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        BalanceLabel(balance = trip.myBalance)
                    }
                }

                // Participantes envolvidos
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Participantes (${trip.participants.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    AvatarStack(participants = trip.participants, maxVisible = 6)
                }

                // Botão de Fechar
                PrimaryButton(
                    text = "Fechar Resumo",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
    }
}