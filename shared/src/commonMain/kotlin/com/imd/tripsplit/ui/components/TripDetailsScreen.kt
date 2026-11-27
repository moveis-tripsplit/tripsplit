package com.imd.tripsplit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.ui.screens.home.trip.components.participants.AddParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.DeleteParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.EditParticipantDialog
import com.imd.tripsplit.ui.screens.home.trip.components.participants.TripParticipantsCard
import com.imd.tripsplit.ui.screens.home.trip.participants.TripParticipantsAction
import com.imd.tripsplit.ui.screens.home.trip.participants.TripParticipantsViewModel
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripParticipantsViewModel = remember { TripParticipantsViewModel() },
) {
    val trip = remember(tripId) { TripRepository.getTrip(tripId) }
    val currentUser by com.imd.tripsplit.data.LocalDataManager.currentUser.collectAsState()
    val isUserCreator = trip != null && trip.isUserCreator(currentUser?.id, currentUser?.email)
    val isUserOrganizer = trip != null && trip.isUserOrganizer(currentUser?.id, currentUser?.email)
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(tripId) {
        viewModel.onAction(TripParticipantsAction.Load(tripId))
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onAction(TripParticipantsAction.ClearError)
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onAction(TripParticipantsAction.ClearSuccess)
        }
    }

    // Modal Adicionar Viajante
    if (uiState.isAddModalOpen) {
        AddParticipantDialog(
            isCreator = isUserCreator,
            onDismiss = { viewModel.onAction(TripParticipantsAction.CloseAddModal) },
            onConfirm = { participant ->
                viewModel.onAction(
                    TripParticipantsAction.AddParticipant(
                        name = participant.name,
                        email = participant.email,
                        role = participant.role,
                        contactOrPix = participant.contactOrPix,
                        hasAccount = participant.hasAccount,
                    )
                )
            },
        )
    }

    // Modal Editar Viajante
    uiState.editingParticipant?.let { participantToEdit ->
        EditParticipantDialog(
            participant = participantToEdit,
            errorMessage = uiState.errorMessage,
            isCreatorUser = isUserCreator,
            onDismiss = { viewModel.onAction(TripParticipantsAction.CloseEditModal) },
            onConfirm = { updated ->
                viewModel.onAction(TripParticipantsAction.UpdateParticipant(updated))
            },
        )
    }

    // Modal Excluir Viajante
    uiState.deletingParticipant?.let { participantToDelete ->
        DeleteParticipantDialog(
            participant = participantToDelete,
            errorMessage = uiState.errorMessage,
            onDismiss = { viewModel.onAction(TripParticipantsAction.CloseDeleteConfirmation) },
            onConfirm = {
                viewModel.onAction(TripParticipantsAction.DeleteParticipant(participantToDelete.id))
            },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(trip?.title ?: "Detalhes da Viagem") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Voltar")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = SpacingLG)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(SpacingMD),
        ) {
            Spacer(modifier = Modifier.height(SpacingSM))

            if (trip != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(modifier = Modifier.padding(SpacingMD)) {
                        Text(
                            text = trip.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (trip.description.isNotBlank()) {
                            Text(
                                text = trip.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            )
                        }
                        Spacer(modifier = Modifier.height(SpacingSM))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Período: ${trip.periodLabel()}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            BalanceLabel(trip.myBalance)
                        }
                    }
                }
            }

            // Gerenciamento de Viajantes (CRUD Completo)
            TripParticipantsCard(
                participants = uiState.participants,
                onAddParticipantClick = { viewModel.onAction(TripParticipantsAction.OpenAddModal) },
                onEditParticipant = { viewModel.onAction(TripParticipantsAction.OpenEditModal(it)) },
                onDeleteParticipant = { viewModel.onAction(TripParticipantsAction.OpenDeleteConfirmation(it)) },
                isOrganizer = isUserOrganizer,
                isCreator = isUserCreator,
                currentUserId = currentUser?.id,
                errorMessage = uiState.errorMessage,
            )

            Spacer(modifier = Modifier.height(SpacingLG))
        }
    }
}
