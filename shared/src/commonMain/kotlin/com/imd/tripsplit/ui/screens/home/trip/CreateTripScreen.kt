package com.imd.tripsplit.ui.screens.home.trip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imd.tripsplit.domain.repository.TripRepository
import com.imd.tripsplit.domain.usecase.CreateTripUseCase
import com.imd.tripsplit.domain.usecase.GetTripUseCase
import com.imd.tripsplit.domain.usecase.UpdateTripUseCase
import com.imd.tripsplit.presentation.trip.CreateTripAction
import com.imd.tripsplit.presentation.trip.CreateTripViewModel
import com.imd.tripsplit.ui.screens.home.trip.components.AddActivityDialog
import com.imd.tripsplit.ui.screens.home.trip.components.AddParticipantDialog

@Composable
fun CreateTripScreen(
    tripRepository: TripRepository,
    modifier: Modifier = Modifier,
    tripId: String? = null,
    onBackClick: () -> Unit = {},
    onTripCreatedSuccess: (tripId: String) -> Unit = {},
) {
    val viewModel: CreateTripViewModel =
        viewModel(key = tripId ?: "new-trip") {
            CreateTripViewModel(
                tripId = tripId,
                getTrip = GetTripUseCase(tripRepository),
                createTrip = CreateTripUseCase(tripRepository),
                updateTrip = UpdateTripUseCase(tripRepository),
            )
        }
    val state by viewModel.state.collectAsState()

    if (state.showAddActivityModal) {
        AddActivityDialog(
            onDismiss = { viewModel.onAction(CreateTripAction.CloseAddActivityModal) },
            onConfirm = { activity -> viewModel.onAction(CreateTripAction.AddActivity(activity)) },
        )
    }

    if (state.showAddParticipantModal) {
        AddParticipantDialog(
            onDismiss = { viewModel.onAction(CreateTripAction.CloseAddParticipantModal) },
            onConfirm = { participant -> viewModel.onAction(CreateTripAction.AddParticipant(participant)) },
        )
    }

    CreateTripContent(
        state = state,
        onAction = viewModel::onAction,
        onSubmitClick = { viewModel.submit(onTripCreatedSuccess) },
        onBackClick = onBackClick,
        isEditing = state.isEditing,
        modifier = modifier,
    )
}
