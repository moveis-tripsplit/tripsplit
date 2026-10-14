package com.imd.tripsplit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.ui.components.AccentButton
import com.imd.tripsplit.ui.components.EmptyState
import com.imd.tripsplit.ui.components.FilterChipRow
import com.imd.tripsplit.ui.components.TripCard
import com.imd.tripsplit.ui.components.TripSplitLogo
import com.imd.tripsplit.ui.components.TripSummaryDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    trips: List<Trip>,
    selectedFilter: TripFilter,
    onFilterSelected: (TripFilter) -> Unit,
    onNavigateToTripEdit: (tripId: String) -> Unit, // Redireciona para edição (gastos/participantes)
    onCreateTripClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Estado para controlar qual viagem está exibindo o pop-up de resumo
    var summaryTrip by remember { mutableStateOf<Trip?>(null) }

    val filteredTrips = when (selectedFilter) {
        TripFilter.ALL -> trips
        TripFilter.ONGOING -> trips.filter { it.status == TripStatus.ONGOING }
        TripFilter.UPCOMING -> trips.filter { it.status == TripStatus.UPCOMING }
        TripFilter.FINISHED -> trips.filter { it.status == TripStatus.FINISHED }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TripSplitLogo(size = 36.dp)
                        Text("TripSplit")
                    }
                },
                actions = {
                    AccentButton(
                        text = "+ Nova Viagem",
                        onClick = onCreateTripClick,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FilterChipRow(
                selected = selectedFilter,
                onSelected = onFilterSelected,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            if (filteredTrips.isEmpty()) {
                EmptyState(
                    title = "Nenhuma viagem encontrada",
                    message = "Você ainda não possui viagens nesta categoria.",
                    actionText = "Criar Viagem",
                    onAction = onCreateTripClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredTrips, key = { it.id }) { trip ->
                        TripCard(
                            trip = trip,
                            onClick = {
                                if (trip.status == TripStatus.FINISHED) {
                                    // Abre o resumo em tela sobreposta (Diálogo)
                                    summaryTrip = trip
                                } else {
                                    // Redireciona para a página de edição/detalhes da viagem em andamento/futura
                                    onNavigateToTripEdit(trip.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Exibe o diálogo de resumo se houver uma viagem selecionada
    summaryTrip?.let { trip ->
        TripSummaryDialog(
            trip = trip,
            onDismiss = { summaryTrip = null }
        )
    }
}