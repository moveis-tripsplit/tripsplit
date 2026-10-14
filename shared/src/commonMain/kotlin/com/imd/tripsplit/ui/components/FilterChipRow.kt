package com.imd.tripsplit.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.TripFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipRow(
    selected: TripFilter,
    onSelected: (TripFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        TripFilter.ALL to "Todas",
        TripFilter.ONGOING to "Em andamento",
        TripFilter.UPCOMING to "Futuras",
        TripFilter.FINISHED to "Finalizadas"
    )

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { (filter, label) ->
            FilterChip(
                selected = selected == filter,
                onClick = { onSelected(filter) },
                label = { Text(label) }
            )
        }
    }
}