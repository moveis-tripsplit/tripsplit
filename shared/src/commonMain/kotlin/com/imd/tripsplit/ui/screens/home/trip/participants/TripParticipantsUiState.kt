package com.imd.tripsplit.ui.screens.home.trip.participants

import com.imd.tripsplit.ui.screens.home.trip.TripParticipant

data class TripParticipantsUiState(
    val tripId: String = "",
    val participants: List<TripParticipant> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAddModalOpen: Boolean = false,
    val editingParticipant: TripParticipant? = null,
    val deletingParticipant: TripParticipant? = null,
)

sealed interface TripParticipantsAction {
    data class Load(val tripId: String) : TripParticipantsAction
    data object OpenAddModal : TripParticipantsAction
    data object CloseAddModal : TripParticipantsAction
    data class OpenEditModal(val participant: TripParticipant) : TripParticipantsAction
    data object CloseEditModal : TripParticipantsAction
    data class OpenDeleteConfirmation(val participant: TripParticipant) : TripParticipantsAction
    data object CloseDeleteConfirmation : TripParticipantsAction

    data class AddParticipant(
        val name: String,
        val email: String?,
        val role: String,
        val contactOrPix: String?,
        val hasAccount: Boolean,
    ) : TripParticipantsAction

    data class UpdateParticipant(val participant: TripParticipant) : TripParticipantsAction
    data class DeleteParticipant(val participantId: String, val hasFinancialPending: Boolean = false) : TripParticipantsAction
    data object ClearError : TripParticipantsAction
    data object ClearSuccess : TripParticipantsAction
}
