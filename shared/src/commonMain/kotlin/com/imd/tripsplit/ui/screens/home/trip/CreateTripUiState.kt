package com.imd.tripsplit.ui.screens.home.trip

import kotlinx.serialization.Serializable

/**
 * Representa uma atividade no roteiro da viagem.
 * Observação: O usuário pode criar a viagem sem atividades e adicioná-las posteriormente.
 */
@Serializable
data class TripActivity(
    val id: String,
    val title: String,
    val dateHour: String,
    val location: String,
    val estimatedCost: Double? = null,
)

/**
 * Representa um participante da viagem.
 * Observação: Participantes podem ser adicionados agora ou convidados posteriormente.
 * Não é obrigatório possuir conta cadastrada no momento da criação.
 */
@Serializable
data class TripParticipant(
    val id: String,
    val name: String,
    val initials: String,
    val role: String = "Participante",
    val isOrganizer: Boolean = false,
    val hasAccount: Boolean = true,
    val contactOrPix: String? = null,
    val email: String? = null,
    val userId: String? = null,
)

/**
 * Estado da tela de Cadastro de Viagem do TripSplit.
 */
data class CreateTripUiState(
    val title: String = "",
    val description: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val activities: List<TripActivity> = emptyList(),
    val participants: List<TripParticipant> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val participantErrorMessage: String? = null,
    val showAddActivityModal: Boolean = false,
    val showAddParticipantModal: Boolean = false,
    val editingParticipant: TripParticipant? = null,
    val deletingParticipant: TripParticipant? = null,
)

/**
 * Ações disparadas pela interface de Cadastro de Viagem (Fluxo Unidirecional de Dados / MVI).
 */
sealed interface CreateTripAction {
    data class UpdateTitle(val title: String) : CreateTripAction

    data class UpdateDescription(val description: String) : CreateTripAction

    data class UpdateStartDate(val startDate: String) : CreateTripAction

    data class UpdateEndDate(val endDate: String) : CreateTripAction

    data object OpenAddActivityModal : CreateTripAction

    data object CloseAddActivityModal : CreateTripAction

    data class AddActivity(val activity: TripActivity) : CreateTripAction

    data class RemoveActivity(val activityId: String) : CreateTripAction

    data object OpenAddParticipantModal : CreateTripAction

    data object CloseAddParticipantModal : CreateTripAction

    data class AddParticipant(val participant: TripParticipant) : CreateTripAction

    data class RemoveParticipant(val participantId: String) : CreateTripAction

    data class OpenEditParticipantModal(val participant: TripParticipant) : CreateTripAction

    data object CloseEditParticipantModal : CreateTripAction

    data class UpdateParticipant(val participant: TripParticipant) : CreateTripAction

    data class OpenDeleteParticipantModal(val participant: TripParticipant) : CreateTripAction

    data object CloseDeleteParticipantModal : CreateTripAction

    data class ConfirmDeleteParticipant(val participantId: String, val hasFinancialPending: Boolean = false) : CreateTripAction

    data object DismissParticipantError : CreateTripAction
}

/**
 * Reducer puro para transição de estado previsível.
 */
fun CreateTripUiState.reduce(action: CreateTripAction): CreateTripUiState =
    when (action) {
        is CreateTripAction.UpdateTitle -> copy(title = action.title)
        is CreateTripAction.UpdateDescription -> copy(description = action.description)
        is CreateTripAction.UpdateStartDate -> copy(startDate = action.startDate)
        is CreateTripAction.UpdateEndDate -> copy(endDate = action.endDate)
        is CreateTripAction.OpenAddActivityModal -> copy(showAddActivityModal = true)
        is CreateTripAction.CloseAddActivityModal -> copy(showAddActivityModal = false)
        is CreateTripAction.AddActivity -> copy(activities = activities + action.activity, showAddActivityModal = false)
        is CreateTripAction.RemoveActivity -> copy(activities = activities.filterNot { it.id == action.activityId })
        is CreateTripAction.OpenAddParticipantModal -> copy(showAddParticipantModal = true, participantErrorMessage = null)
        is CreateTripAction.CloseAddParticipantModal -> copy(showAddParticipantModal = false)
        is CreateTripAction.AddParticipant -> copy(participants = participants + action.participant, showAddParticipantModal = false)
        is CreateTripAction.RemoveParticipant -> copy(participants = participants.filterNot { it.id == action.participantId })
        is CreateTripAction.OpenEditParticipantModal -> copy(editingParticipant = action.participant, participantErrorMessage = null)
        is CreateTripAction.CloseEditParticipantModal -> copy(editingParticipant = null)
        is CreateTripAction.UpdateParticipant -> {
            val current = participants.find { it.id == action.participant.id }
            if (current?.isOrganizer == true && !action.participant.isOrganizer && participants.count { it.isOrganizer } <= 1) {
                copy(participantErrorMessage = "A viagem deve ter pelo menos um Organizador. Defina outro organizador antes de alterar esta função.")
            } else {
                copy(
                    participants = participants.map { if (it.id == action.participant.id) action.participant else it },
                    editingParticipant = null,
                    participantErrorMessage = null,
                )
            }
        }
        is CreateTripAction.OpenDeleteParticipantModal -> copy(deletingParticipant = action.participant, participantErrorMessage = null)
        is CreateTripAction.CloseDeleteParticipantModal -> copy(deletingParticipant = null)
        is CreateTripAction.ConfirmDeleteParticipant -> {
            val target = participants.find { it.id == action.participantId }
            if (target?.isOrganizer == true && participants.count { it.isOrganizer } <= 1) {
                copy(participantErrorMessage = "Toda viagem deve ter obrigatoriamente pelo menos um Organizador. Transfira a função antes de excluir.")
            } else if (action.hasFinancialPending) {
                copy(participantErrorMessage = "Não é possível excluir viajante que possui despesas ativas vinculadas ou débitos em aberto.")
            } else {
                copy(
                    participants = participants.filterNot { it.id == action.participantId },
                    deletingParticipant = null,
                    participantErrorMessage = null,
                )
            }
        }
        is CreateTripAction.DismissParticipantError -> copy(participantErrorMessage = null)
    }
