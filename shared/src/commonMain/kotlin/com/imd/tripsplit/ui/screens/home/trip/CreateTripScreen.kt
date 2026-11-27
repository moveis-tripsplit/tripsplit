package com.imd.tripsplit.ui.screens.home.trip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.imd.tripsplit.ui.screens.home.trip.components.participants.AddParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.DeleteParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.EditParticipantDialog
import kotlin.random.Random

@Composable
fun CreateTripScreen(
    modifier: Modifier = Modifier,
    tripId: String? = null,
    onBackClick: () -> Unit = {},
    onTripCreatedSuccess: (tripId: String) -> Unit = {},
) {
    val currentUser by com.imd.tripsplit.data.UserRepository.currentUser.collectAsState()
    val initialParticipants =
        remember(currentUser) {
            val current = currentUser
            if (current != null) {
                listOf(
                    TripParticipant(
                        id = current.id,
                        name = current.name,
                        initials =
                            current.name
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .map { it.first().uppercase() }
                                .take(2)
                                .joinToString("")
                                .ifEmpty { "O" },
                        role = "Organizador",
                        isOrganizer = true,
                        hasAccount = true,
                        email = current.email,
                        userId = current.id,
                        isCreator = true,
                    ),
                )
            } else {
                emptyList()
            }
        }
    val editingTrip = remember(tripId) { tripId?.let { TripRepository.getTrip(it) } }
    val isUserCreator = editingTrip == null || editingTrip.isUserCreator(currentUser?.id, currentUser?.email)
    val isUserOrganizer = editingTrip == null || editingTrip.isUserOrganizer(currentUser?.id, currentUser?.email)

    var state by remember(tripId) {
        mutableStateOf(editingTrip?.toUiState() ?: CreateTripUiState(participants = initialParticipants))
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
        if (!isUserCreator) {
            state = state.copy(showAddParticipantModal = false, participantErrorMessage = "Apenas o criador da viagem pode adicionar participantes.")
        } else {
            AddParticipantDialog(
                isCreator = isUserCreator,
                onDismiss = { state = state.reduce(CreateTripAction.CloseAddParticipantModal) },
                onConfirm = { participant ->
                    state = state.reduce(CreateTripAction.AddParticipant(participant))
                    if (editingTrip != null) {
                        TripRepository.updateTrip(editingTrip.copy(participants = state.participants.map { it.toParticipant() }))
                    }
                },
            )
        }
    }

    // Diálogo modal para editar participante
    state.editingParticipant?.let { participantToEdit ->
        if (!isUserOrganizer) {
            state = state.copy(editingParticipant = null, participantErrorMessage = "Participantes não têm permissão para editar participantes. Apenas organizadores têm essa permissão.")
        } else {
            EditParticipantDialog(
                participant = participantToEdit,
                errorMessage = state.participantErrorMessage,
                isCreatorUser = isUserCreator,
                onDismiss = { state = state.reduce(CreateTripAction.CloseEditParticipantModal) },
                onConfirm = { updated ->
                    state = state.reduce(CreateTripAction.UpdateParticipant(updated))
                    if (editingTrip != null && state.participantErrorMessage == null) {
                        TripRepository.updateTrip(editingTrip.copy(participants = state.participants.map { it.toParticipant() }))
                    }
                },
            )
        }
    }

    // Diálogo modal para confirmar exclusão de participante
    state.deletingParticipant?.let { participantToDelete ->
        if (!isUserOrganizer) {
            state = state.copy(deletingParticipant = null, participantErrorMessage = "Apenas organizadores podem excluir participantes da viagem.")
        } else {
            DeleteParticipantDialog(
                participant = participantToDelete,
                errorMessage = state.participantErrorMessage,
                onDismiss = { state = state.reduce(CreateTripAction.CloseDeleteParticipantModal) },
                onConfirm = {
                    state = state.reduce(CreateTripAction.ConfirmDeleteParticipant(participantToDelete.id))
                    if (editingTrip != null && state.participantErrorMessage == null) {
                        TripRepository.updateTrip(editingTrip.copy(participants = state.participants.map { it.toParticipant() }))
                    }
                },
            )
        }
    }

    CreateTripContent(
        state = state,
        onAction = { action -> state = state.reduce(action) },
        onSubmitClick = {
            if (editingTrip != null) {
                if (!isUserCreator) {
                    onBackClick()
                    return@CreateTripContent
                }
                val updateResult =
                    TripRepository.updateTrip(
                        editingTrip.copy(
                            title = state.title.trim(),
                            description = state.description.trim(),
                            startDate = state.startDate,
                            endDate = state.endDate,
                            participants =
                                state.participants.map { p ->
                                    val isCreator = p.isCreator || (editingTrip.userId.isNotBlank() && (p.userId == editingTrip.userId || p.id == editingTrip.userId))
                                    p.toParticipant().copy(
                                        isCreator = isCreator,
                                        isOrganizer = if (isCreator) true else p.isOrganizer,
                                        role = if (isCreator) "Organizador" else p.role,
                                    )
                                },
                            activities = state.activities,
                        ),
                        requesterUserId = currentUser?.id,
                        requesterEmail = currentUser?.email,
                    )
                if (updateResult.isSuccess) {
                    onTripCreatedSuccess(editingTrip.id)
                }
            } else {
                val current = currentUser
                val currentUserId = current?.id ?: ""
                val defaultParticipants =
                    state.participants.map { p ->
                        val isCreator = p.isCreator || (currentUserId.isNotBlank() && (p.userId == currentUserId || p.id == currentUserId))
                        p.toParticipant().copy(
                            isCreator = isCreator,
                            isOrganizer = if (isCreator) true else p.isOrganizer,
                            role = if (isCreator) "Organizador" else p.role,
                        )
                    }.let { list ->
                        if (current != null && list.none { it.isCreator || (currentUserId.isNotBlank() && (it.userId == currentUserId || it.id == currentUserId)) }) {
                            listOf(
                                com.imd.tripsplit.model.Participant(
                                    id = current.id,
                                    name = current.name,
                                    role = "Organizador",
                                    isOrganizer = true,
                                    email = current.email,
                                    userId = current.id,
                                    hasAccount = true,
                                    isCreator = true,
                                ),
                            ) + list
                        } else {
                            list
                        }
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
        isOrganizer = isUserOrganizer,
        isCreator = isUserCreator,
        currentUserId = currentUser?.id,
        isEditing = editingTrip != null,
        modifier = modifier,
    )
}
