package com.imd.tripsplit.ui.screens.home.trip

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.imd.tripsplit.ui.screens.home.trip.components.AddActivityDialog
import com.imd.tripsplit.ui.screens.home.trip.components.AddParticipantDialog
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
    val editingActivityId = state.editingActivityId

    val initialActivity =
        editingActivityId?.let { activityId ->
            state.activities.firstOrNull { it.id == activityId }
        }

    if (
        state.showAddActivityModal &&
        (editingActivityId == null || initialActivity != null)
    ) {
        AddActivityDialog(
            availableParticipants = state.participants,
            initialActivity = initialActivity,
            onDismiss = { state = state.reduce(CreateTripAction.CloseAddActivityModal,) },
            onConfirm = { activity ->
                val action =
                    if (editingActivityId == null) {
                        CreateTripAction.AddActivity(activity)
                    } else {
                        CreateTripAction.UpdateActivity(activity)
                    }

                state = state.reduce(action)
            },
        )

    }

    val pendingDeleteActivityId = state.pendingDeleteActivityId

    if (pendingDeleteActivityId != null) {
        val activityToDelete =
            state.activities.firstOrNull {
                it.id == pendingDeleteActivityId
            }

        if (pendingDeleteActivityId != null && activityToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    state = state.reduce(
                        CreateTripAction.CancelRemoveActivity,
                    )
                },
                title = {
                    Text(
                        text = "Excluir atividade?",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                text = {
                    Text(
                        text =
                            activityToDelete?.let {
                                "Deseja remover \"${activityToDelete.title}\" do roteiro?"
                            } ?: "Deseja remover esta atividade do roteiro?",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            state = state.reduce(
                                CreateTripAction.RemoveActivity(
                                    pendingDeleteActivityId,
                                ),
                            )
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.errorContainer,
                                contentColor =
                                    MaterialTheme.colorScheme.onErrorContainer,
                            ),
                    ) {
                        Text(
                            text = "Excluir",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            state = state.reduce(
                                CreateTripAction.CancelRemoveActivity,
                            )
                        },
                    ) {
                        Text(
                            text = "Cancelar",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
            )
        }
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
                val current = com.imd.tripsplit.data.UserRepository.currentUser.value
                val currentUserId = current?.id ?: ""
                val defaultParticipants =
                    if (state.participants.isEmpty() && current != null) {
                        listOf(
                            com.imd.tripsplit.model.Participant(
                                id = current.id,
                                name = current.name,
                                role = "Organizador",
                                isOrganizer = true,
                            ),
                        )
                    } else {
                        state.participants.map { it.toParticipant() }
                    }

                val trip =
                    Trip(
                        id = "trip-${Random.nextInt(1000, 9999)}",
                        userId = currentUserId,
                        title = state.title.trim(),
                        description = state.description.trim(),
                        destination = state.title.trim(),
                        status = TripStatus.UPCOMING,
                        totalAmount = state.activities.sumOf { it.estimatedCost ?: 0.0 },
                        myBalance = 0.0,
                        startDate = state.startDate,
                        endDate = state.endDate,
                        participants = defaultParticipants,
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
