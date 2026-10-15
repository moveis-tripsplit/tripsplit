package com.imd.tripsplit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingSM

/* ------------------------------------------------------------------ */
/* Filtros: selecionado = Primary (azul é a cor de seleção), pílula    */
/* ------------------------------------------------------------------ */
@Composable
fun FilterChipRow(
    selected: TripFilter,
    onSelected: (TripFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(50)
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingSM),
    ) {
        items(TripFilter.entries.toList()) { filter ->
            val isSelected = filter == selected
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 40.dp)
                    .clip(shape)
                    .background(if (isSelected) scheme.primary else scheme.surface)
                    .border(1.dp, if (isSelected) scheme.primary else scheme.outline, shape)
                    .clickable { onSelected(filter) }
                    .padding(horizontal = SpacingLG),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = filter.displayName(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = if (isSelected) scheme.onPrimary else scheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun TripFilter.displayName(): String = when (this) {
    TripFilter.ALL -> "Todas"
    TripFilter.ONGOING -> "Em andamento"
    TripFilter.UPCOMING -> "Futuras"
    TripFilter.FINISHED -> "Finalizadas"
}