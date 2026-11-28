package com.imd.tripsplit.ui.screens.home.trip

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.data.toParticipant
import com.imd.tripsplit.domain.activityDateError
import com.imd.tripsplit.domain.tripDateRange
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.model.toUiState
import com.imd.tripsplit.ui.screens.home.trip.components.AddActivityDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.AddParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.DeleteParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.EditParticipantDialog
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.update
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.update

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

    val formState = remember(tripId) {
        MutableStateFlow(
            editingTrip?.toUiState()
                ?: CreateTripUiState(participants = initialParticipants),
        )
    }
    val state by formState.collectAsState()

    val pendingRemoteDeletions = remember(tripId) { mutableSetOf<String>() }

    val changedActivityIds = remember(tripId) { mutableSetOf<String>() }

    var loadingActivities by remember(tripId) { mutableStateOf(false) }
    var activityLoadError by remember(tripId) { mutableStateOf<String?>(null) }
    var activityLoadAttempt by remember(tripId) { mutableStateOf(0) }

    LaunchedEffect(formState, editingTrip?.id, activityLoadAttempt) {
        val savedTrip = editingTrip ?: return@LaunchedEffect

        if (
            savedTrip.pendingActivityUpsertIds.isNotEmpty() ||
            savedTrip.pendingActivityDeleteIds.isNotEmpty() ||
            changedActivityIds.isNotEmpty() ||
            pendingRemoteDeletions.isNotEmpty()
        ) {
            return@LaunchedEffect
        }

        val activitiesAtStart = formState.value.activities

        loadingActivities = true
        activityLoadError = null

        try {
            TripRepository.fetchActivities(savedTrip.id)
                .onSuccess { remoteActivities ->
                    formState.update { current ->
                        val editingRoster =
                            current.showAddActivityModal ||
                                    current.pendingDeleteActivityId != null

                        if (
                            editingRoster ||
                            current.activities != activitiesAtStart ||
                            changedActivityIds.isNotEmpty() ||
                            pendingRemoteDeletions.isNotEmpty()
                        ) {                            current
                        } else {
                            val localByRemoteId = current.activities
                                .filter { it.remoteId != null }
                                .associateBy { it.remoteId }

                            val drafts = current.activities.filter { it.remoteId == null }
                            val usedIds = drafts.mapTo(mutableSetOf()) { it.id }

                            val syncedActivities = remoteActivities
                                .filterNot { remote -> remote.remoteId in pendingRemoteDeletions }
                                .map { remote ->                                val previous = localByRemoteId[remote.remoteId]
                                var localId = previous?.id ?: remote.id

                                while (!usedIds.add(localId)) {
                                    localId = "remote-$localId"
                                }

                                remote.copy(id = localId)
                            }

                            current.copy(activities = drafts + syncedActivities)
                        }
                    }
                }
                .onFailure {
                    activityLoadError =
                        "Não foi possível atualizar o roteiro. As atividades locais continuam disponíveis."
                }
        } finally {
            loadingActivities = false
        }
    }

    var tripDateSaveError by remember(tripId) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    var savingParticipant by remember(tripId) { mutableStateOf(false) }
    var savingTrip by remember(tripId) { mutableStateOf(false) }
    var activitySyncError by remember(tripId) { mutableStateOf<String?>(null) }
    var locallySavedTripId by remember(tripId) { mutableStateOf<String?>(null) }
    var participantSaveError by remember(tripId) { mutableStateOf<String?>(null) }

    fun applyParticipantChange(
        action: CreateTripAction,
        persist: suspend (Trip) -> Result<*>,
    ) {
        if (savingParticipant) return

        val nextState = formState.value.reduce(action)
        if (editingTrip == null || nextState.participantErrorMessage != null) {
            formState.value = nextState
            return
        }

        savingParticipant = true
        scope.launch {
            try {
                persist(editingTrip)
                    .onSuccess {
                        formState.value = nextState
                    }
                    .onFailure { error ->
                        participantSaveError =
                            error.message ?: "Não foi possível salvar a alteração do participante."
                    }
            } finally {
                savingParticipant = false
            }
        }
    }

    participantSaveError?.let { message ->
        AlertDialog(
            onDismissRequest = { participantSaveError = null },
            title = { Text("Não foi possível alterar o participante") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { participantSaveError = null }) {
                    Text("Entendi")
                }
            },
        )
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
            startDate = state.startDate,
            endDate = state.endDate,
            initialActivity = initialActivity,
            onDismiss = {
                formState.value = formState.value.reduce(CreateTripAction.CloseAddActivityModal)
            },            onConfirm = { activity ->
                val action =
                    if (editingActivityId == null) {
                        CreateTripAction.AddActivity(activity)
                    } else {
                        CreateTripAction.UpdateActivity(activity)
                    }

                changedActivityIds.add(activity.id)

                formState.value = formState.value.reduce(action)
            },
        )

    }

    val pendingDeleteActivityId = state.pendingDeleteActivityId

    if (pendingDeleteActivityId != null) {
        val activityToDelete =
            state.activities.firstOrNull {
                it.id == pendingDeleteActivityId
            }

        if (activityToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    formState.value = formState.value.reduce(
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
                        text = "Deseja remover \"${activityToDelete.title}\" do roteiro?",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {

                            changedActivityIds.remove(activityToDelete.id)

                            activityToDelete.remoteId?.let { remoteId ->
                                pendingRemoteDeletions.add(remoteId)
                            }
                            changedActivityIds.remove(activityToDelete.id)

                            formState.update { current ->
                                current.reduce(
                                    CreateTripAction.RemoveActivity(activityToDelete.id),
                                )
                            }
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
                            formState.value = formState.value.reduce(
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
        if (!isUserCreator) {
            LaunchedEffect(formState) {
                formState.update { current ->
                    current.copy(
                        showAddParticipantModal = false,
                        participantErrorMessage = "Apenas o criador da viagem pode adicionar participantes.",
                    )
                }
            }        } else {
            AddParticipantDialog(
                isCreator = isUserCreator,
                onDismiss = {
                    if (!savingParticipant) {
                        formState.update { current ->
                            current.reduce(CreateTripAction.CloseAddParticipantModal)
                        }
                    }
                },
                onConfirm = { participant ->
                    applyParticipantChange(CreateTripAction.AddParticipant(participant)) { trip ->
                        TripRepository.addParticipant(
                            tripId = trip.id,
                            participant = participant.toParticipant(),
                            requesterUserId = currentUser?.id,
                            requesterEmail = currentUser?.email,
                        )
                    }
                },
            )
        }
    }

    // Diálogo modal para editar participante
    state.editingParticipant?.let { participantToEdit ->
        if (!isUserOrganizer) {
            LaunchedEffect(formState, participantToEdit.id) {
                formState.update { current ->
                    current.copy(
                        editingParticipant = null,
                        participantErrorMessage = "Participantes não têm permissão para editar participantes. Apenas organizadores têm essa permissão.",
                    )
                }
            }        } else {
            EditParticipantDialog(
                participant = participantToEdit,
                errorMessage = state.participantErrorMessage,
                isCreatorUser = isUserCreator,
                onDismiss = {
                    if (!savingParticipant) {
                        formState.value = formState.value.reduce(CreateTripAction.CloseEditParticipantModal)
                    }
                },
                onConfirm = { updated ->
                    applyParticipantChange(CreateTripAction.UpdateParticipant(updated)) { trip ->
                        TripRepository.updateParticipant(
                            tripId = trip.id,
                            participant = updated.toParticipant(),
                            requesterUserId = currentUser?.id,
                            requesterEmail = currentUser?.email,
                        )
                    }
                },
            )
        }
    }

    // Diálogo modal para confirmar exclusão de participante
    state.deletingParticipant?.let { participantToDelete ->
        if (!isUserOrganizer) {
            LaunchedEffect(formState, participantToDelete.id) {
                formState.update { current ->
                    current.copy(
                        deletingParticipant = null,
                        participantErrorMessage = "Apenas organizadores podem excluir participantes da viagem.",
                    )
                }
            }        } else {
            DeleteParticipantDialog(
                participant = participantToDelete,
                errorMessage = state.participantErrorMessage,
                onDismiss = {
                    if (!savingParticipant) {
                        formState.value = formState.value.reduce(CreateTripAction.CloseDeleteParticipantModal)
                    }
                },
                onConfirm = {
                    applyParticipantChange(
                        CreateTripAction.ConfirmDeleteParticipant(participantToDelete.id),
                    ) { trip ->
                        TripRepository.removeParticipant(
                            tripId = trip.id,
                            participantId = participantToDelete.id,
                            requesterUserId = currentUser?.id,
                            requesterEmail = currentUser?.email,
                        )
                    }
                },
            )
        }
    }

    tripDateSaveError?.let { message ->
        AlertDialog(
            onDismissRequest = { tripDateSaveError = null },
            title = { Text("Confira o período da viagem") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { tripDateSaveError = null }) {
                    Text("Entendi")
                }
            },
        )
    }

    Column(modifier = modifier) {
        if (loadingActivities) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        activityLoadError?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
            TextButton(onClick = { activityLoadAttempt++ }) {
                Text("Tentar novamente")
            }
        }

        fun finishTripSave(savedTripId: String) {
            savingTrip = true
            locallySavedTripId = savedTripId

            scope.launch {
                try {
                    val result = TripRepository.syncPendingActivities(
                        tripId = savedTripId,
                        requesterUserId = currentUser?.id,
                        requesterEmail = currentUser?.email,
                    )

                    if (result.isSuccess) {
                        onTripCreatedSuccess(savedTripId)
                    } else {
                        val detail = result.exceptionOrNull()?.message.orEmpty()
                        activitySyncError =
                            "A viagem foi salva neste aparelho. A sincronização do roteiro ficou pendente. $detail"
                    }
                } finally {
                    savingTrip = false
                }
            }
        }

        if (savingTrip) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Salvando roteiro") },
                text = { Text("Sincronizando as atividades da viagem…") },
                confirmButton = {},
            )
        }

        activitySyncError?.let { message ->
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Roteiro salvo no aparelho") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val savedId = locallySavedTripId
                            if (savedId != null) {
                                activitySyncError = null
                                finishTripSave(savedId)
                            }
                        },
                    ) {
                        Text("Tentar novamente")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            locallySavedTripId?.let { onTripCreatedSuccess(it) }
                        },
                    ) {
                        Text("Concluir offline")
                    }
                },
            )
        }

        CreateTripContent(
            state = state,
            onAction = { action -> formState.value = formState.value.reduce(action) },
            onSubmitClick = {
                if (savingTrip || savingParticipant || activitySyncError != null) {
                    return@CreateTripContent
                }
                if (editingTrip != null && !isUserCreator) {
                    onBackClick()
                    return@CreateTripContent
                }

                val period = tripDateRange(state.startDate, state.endDate)
                if (period == null) {
                    tripDateSaveError = "Confira as datas de início e término da viagem."
                    return@CreateTripContent
                }

                for (activity in state.activities) {
                    val error = activityDateError(activity.dateHour, period)
                    if (error != null) {
                        tripDateSaveError = "Confira a atividade “${activity.title}”. $error"
                        return@CreateTripContent
                    }
                }

                if (editingTrip != null) {
                    val latestTrip = TripRepository.getTrip(editingTrip.id) ?: editingTrip
                    val presentIds = state.activities.map { it.id }.toSet()
                    val updateResult =
                        TripRepository.updateTrip(
                            latestTrip.copy(
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
                                pendingActivityUpsertIds =
                                    (latestTrip.pendingActivityUpsertIds + changedActivityIds)
                                        .distinct()
                                        .filter { it in presentIds },
                                pendingActivityDeleteIds =
                                    (latestTrip.pendingActivityDeleteIds + pendingRemoteDeletions).distinct(),
                            ),
                            requesterUserId = currentUser?.id,
                            requesterEmail = currentUser?.email,
                        )
                    updateResult
                        .onSuccess { finishTripSave(it.id) }
                        .onFailure { error ->
                            tripDateSaveError = error.message ?: "Não foi possível salvar a viagem."
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
                            pendingActivityUpsertIds = state.activities.map { it.id }.distinct(),
                        )
                    TripRepository.addTrip(trip)
                    finishTripSave(trip.id)
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
}
