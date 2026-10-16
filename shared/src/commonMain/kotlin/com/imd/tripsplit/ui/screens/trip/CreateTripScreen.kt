package com.imd.tripsplit.ui.screens.trip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.imd.tripsplit.ui.screens.trip.components.AddActivityDialog
import com.imd.tripsplit.ui.screens.trip.components.AddParticipantDialog
import kotlin.random.Random

/**
 * Versão Stateful (com estado):
 * Gerencia o ciclo de vida e estado da tela de cadastro de viagens,
 * orquestra modais e delega as ações via Reducer/MVI para a [CreateTripContent].
 */
@Composable
fun CreateTripScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onTripCreatedSuccess: (tripId: String) -> Unit = {},
) {
    var state by remember { mutableStateOf(CreateTripUiState()) }

    // Diálogo modal para adicionar atividade ao roteiro
    if (state.showAddActivityModal) {
        AddActivityDialog(
            onDismiss = { state = state.reduce(CreateTripAction.CloseAddActivityModal) },
            onConfirm = { activity -> state = state.reduce(CreateTripAction.AddActivity(activity)) },
        )
    }

    // Diálogo modal para adicionar participante
    if (state.showAddParticipantModal) {
        AddParticipantDialog(
            onDismiss = { state = state.reduce(CreateTripAction.CloseAddParticipantModal) },
            onConfirm = { participant -> state = state.reduce(CreateTripAction.AddParticipant(participant)) },
        )
    }

    CreateTripContent(
        state = state,
        onAction = { action -> state = state.reduce(action) },
        onSubmitClick = {
            onTripCreatedSuccess("trip-${Random.nextInt(100, 999)}")
        },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}
