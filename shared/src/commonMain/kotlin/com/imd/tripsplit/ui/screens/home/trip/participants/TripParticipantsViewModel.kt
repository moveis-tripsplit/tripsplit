package com.imd.tripsplit.ui.screens.home.trip.participants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.data.toParticipant
import com.imd.tripsplit.data.toTripParticipant
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class TripParticipantsViewModel(
    private val repository: TripRepository = TripRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripParticipantsUiState())
    val uiState: StateFlow<TripParticipantsUiState> = _uiState.asStateFlow()

    fun onAction(action: TripParticipantsAction) {
        when (action) {
            is TripParticipantsAction.Load -> load(action.tripId)
            is TripParticipantsAction.OpenAddModal -> _uiState.update { it.copy(isAddModalOpen = true) }
            is TripParticipantsAction.CloseAddModal -> _uiState.update { it.copy(isAddModalOpen = false) }
            is TripParticipantsAction.OpenEditModal -> _uiState.update { it.copy(editingParticipant = action.participant) }
            is TripParticipantsAction.CloseEditModal -> _uiState.update { it.copy(editingParticipant = null) }
            is TripParticipantsAction.OpenDeleteConfirmation -> _uiState.update { it.copy(deletingParticipant = action.participant) }
            is TripParticipantsAction.CloseDeleteConfirmation -> _uiState.update { it.copy(deletingParticipant = null) }
            is TripParticipantsAction.AddParticipant -> addParticipant(action)
            is TripParticipantsAction.UpdateParticipant -> updateParticipant(action.participant)
            is TripParticipantsAction.DeleteParticipant -> deleteParticipant(action.participantId, action.hasFinancialPending)
            is TripParticipantsAction.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is TripParticipantsAction.ClearSuccess -> _uiState.update { it.copy(successMessage = null) }
        }
    }

    private fun load(tripId: String) {
        val currentParticipants = repository.getParticipants(tripId).map { it.toTripParticipant() }
        _uiState.update {
            it.copy(
                tripId = tripId,
                participants = currentParticipants,
                isLoading = false,
            )
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            repository.syncParticipants(tripId)
            val updated = repository.getParticipants(tripId).map { it.toTripParticipant() }
            _uiState.update { it.copy(participants = updated, isSyncing = false) }
        }
    }

    private fun addParticipant(action: TripParticipantsAction.AddParticipant) {
        val tripId = _uiState.value.tripId

        val newParticipant = Participant(
            id = "part-${Random.nextInt(1000, 9999)}",
            name = action.name.trim(),
            email = action.email?.trim()?.ifBlank { null },
            role = action.role.trim().ifBlank { "Participante" },
            isOrganizer = action.role.equals("Organizador", ignoreCase = true),
            contactOrPix = action.contactOrPix?.trim()?.ifBlank { null },
            hasAccount = action.hasAccount,
        )

        viewModelScope.launch {
            val result = repository.addParticipant(tripId, newParticipant)
            if (result.isSuccess) {
                val list = repository.getParticipants(tripId).map { it.toTripParticipant() }
                _uiState.update {
                    it.copy(
                        participants = list,
                        isAddModalOpen = false,
                        successMessage = "Viajante adicionado com sucesso!",
                        errorMessage = null,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = result.exceptionOrNull()?.message ?: "Erro ao adicionar participante."
                    )
                }
            }
        }
    }

    private fun updateParticipant(participant: TripParticipant) {
        val tripId = _uiState.value.tripId
        val domain = participant.toParticipant()

        viewModelScope.launch {
            val result = repository.updateParticipant(tripId, domain)
            if (result.isSuccess) {
                val list = repository.getParticipants(tripId).map { it.toTripParticipant() }
                _uiState.update {
                    it.copy(
                        participants = list,
                        editingParticipant = null,
                        successMessage = "Viajante atualizado com sucesso!",
                        errorMessage = null,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = result.exceptionOrNull()?.message ?: "Erro ao atualizar participante."
                    )
                }
            }
        }
    }

    private fun deleteParticipant(participantId: String, hasFinancialPending: Boolean) {
        val tripId = _uiState.value.tripId

        viewModelScope.launch {
            val result = repository.removeParticipant(tripId, participantId, hasFinancialPending)
            if (result.isSuccess) {
                val list = repository.getParticipants(tripId).map { it.toTripParticipant() }
                _uiState.update {
                    it.copy(
                        participants = list,
                        deletingParticipant = null,
                        successMessage = "Viajante removido com sucesso!",
                        errorMessage = null,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = result.exceptionOrNull()?.message ?: "Erro ao remover participante."
                    )
                }
            }
        }
    }
}
