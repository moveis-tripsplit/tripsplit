package com.imd.tripsplit.ui.screens.trip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.data.toParticipant
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.model.toUiState
import com.imd.tripsplit.ui.screens.trip.components.AddActivityDialog
import com.imd.tripsplit.ui.screens.trip.components.AddParticipantDialog
import kotlin.random.Random

@Composable
fun CreateTripScreen(
    modifier: Modifier = Modifier,
    tripId: String? = null,
    onBackClick: () -> Unit = {},
    onTripCreatedSuccess: (tripId: String) -> Unit = {},
) {
    val editingTrip = remember(tripId) { tripId?.let { TripRepository.getTrip(it) } }
    var state by remember(tripId) {
        mutableStateOf(editingTrip?.toUiState() ?: CreateTripUiState())
    }

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
            if (editingTrip != null) {
                TripRepository.updateTrip(
                    editingTrip.copy(
                        title = state.title.trim(),
                        description = state.description.trim(),
                        startDate = state.startDate,
                        endDate = state.endDate,
                        participants = state.participants.map { it.toParticipant() },
                        activities = state.activities,
                    ),
                )
                onTripCreatedSuccess(editingTrip.id)
            } else {
                val trip =
                    Trip(
                        id = "trip-${Random.nextInt(1000, 9999)}",
                        title = state.title.trim(),
                        description = state.description.trim(),
                        destination = state.title.trim(),
                        status = TripStatus.UPCOMING,
                        totalAmount = state.activities.sumOf { it.estimatedCost ?: 0.0 },
                        myBalance = 0.0,
                        startDate = state.startDate,
                        endDate = state.endDate,
                        participants = state.participants.map { it.toParticipant() },
                        activities = state.activities,
                    )
                TripRepository.addTrip(trip)
                onTripCreatedSuccess(trip.id)
            }
        },
        onBackClick = onBackClick,
        isEditing = editingTrip != null,
        modifier = modifier,
    )
}
