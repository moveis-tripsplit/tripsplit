package com.imd.tripsplit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.ui.theme.TextSecondary
import kotlinx.datetime.LocalDate

private val MonthsPtBr = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")

private fun LocalDate.short(): String =
    "${dayOfMonth.toString().padStart(2, '0')} ${MonthsPtBr[month.ordinal]}"

fun Trip.periodLabel(): String =
    "${startDate.short()} – ${endDate.short()} ${endDate.year}"

@Composable
fun TripCard(
    trip: Trip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TripSplitCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(trip.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    Text("${trip.destination} • ${trip.periodLabel()}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
                StatusChip(trip.status, modifier = Modifier.padding(start = 8.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AvatarStack(trip.participants)
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total ${trip.totalAmount.toBrl()}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    BalanceLabel(trip.myBalance)
                }
            }
        }
    }
}