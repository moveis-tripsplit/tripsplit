package com.imd.tripsplit.ui.screens.home

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.ui.components.AccentButton
import com.imd.tripsplit.ui.components.EmptyState
import com.imd.tripsplit.ui.components.FilterChipRow
import com.imd.tripsplit.ui.components.TripCard
import com.imd.tripsplit.ui.components.TripSplitLogo
import com.imd.tripsplit.ui.components.TripSummaryDialog
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    trips: List<Trip>,
    selectedFilter: TripFilter,
    onFilterSelected: (TripFilter) -> Unit,
    // Redireciona para edição (gastos/participantes)
    onNavigateToTripEdit: (tripId: String) -> Unit,
    onCreateTripClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Estado para controlar qual viagem está exibindo o pop-up de resumo
    var summaryTrip by remember { mutableStateOf<Trip?>(null) }

    val filteredTrips =
        when (selectedFilter) {
            TripFilter.ALL -> trips
            TripFilter.ONGOING -> trips.filter { it.status == TripStatus.ONGOING }
            TripFilter.UPCOMING -> trips.filter { it.status == TripStatus.UPCOMING }
            TripFilter.FINISHED -> trips.filter { it.status == TripStatus.FINISHED }
        }

    // "Trip" escuro + "Split" azul, igual ao título da tela de login (cores do tema, p/ Dark Mode)
    val scheme = MaterialTheme.colorScheme
    val appTitle =
        buildAnnotatedString {
            withStyle(SpanStyle(color = scheme.onBackground, fontWeight = FontWeight.Bold)) { append("Trip") }
            withStyle(SpanStyle(color = scheme.primary, fontWeight = FontWeight.Bold)) { append("Split") }
        }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SpacingSM),
                    ) {
                        TripSplitLogo(size = 36.dp)
                        Text(text = appTitle, fontSize = 22.sp)
                    }
                },
                actions = {
                    AccentButton(
                        text = "+ Nova Viagem",
                        onClick = onCreateTripClick,
                        modifier = Modifier.padding(end = SpacingLG),
                    )
                },
                // Fundo do header = Background do Design System (#F8FAFC)
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background,
                    ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = SpacingLG),
            verticalArrangement = Arrangement.spacedBy(SpacingLG),
        ) {
            FilterChipRow(
                selected = selectedFilter,
                onSelected = onFilterSelected,
                modifier = Modifier.fillMaxWidth().padding(top = SpacingSM),
            )

            if (filteredTrips.isEmpty()) {
                EmptyState(
                    title = "Nenhuma viagem encontrada",
                    message = "Você ainda não possui viagens nesta categoria.",
                    actionText = "Criar Viagem",
                    onAction = onCreateTripClick,
                    modifier = Modifier.weight(1f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SpacingMD),
                    contentPadding = PaddingValues(bottom = SpacingXL),
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
                            },
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
            onDismiss = { summaryTrip = null },
        )
    }
}
