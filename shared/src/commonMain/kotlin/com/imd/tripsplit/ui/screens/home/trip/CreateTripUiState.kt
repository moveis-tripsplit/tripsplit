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
    val participantIds: List<String> = emptyList(),
    val remoteId: String? = null,
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
    val isCreator: Boolean = false,
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
    val editingActivityId: String? = null,
    val pendingDeleteActivityId: String? = null,
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

    data class OpenEditActivityModal(val activityId: String) : CreateTripAction

    data object CloseAddActivityModal : CreateTripAction

    data class AddActivity(val activity: TripActivity) : CreateTripAction

    data class UpdateActivity(val activity: TripActivity) : CreateTripAction

    data class RequestRemoveActivity(val activityId: String) : CreateTripAction

    data object CancelRemoveActivity : CreateTripAction

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

    data class ConfirmDeleteParticipant(
        val participantId: String,
        val hasFinancialPending: Boolean = false,
    ) : CreateTripAction

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

        CreateTripAction.OpenAddActivityModal ->
            copy(showAddActivityModal = true, editingActivityId = null)

        is CreateTripAction.OpenEditActivityModal ->
            if (activities.any { it.id == action.activityId }) {
                copy(showAddActivityModal = true, editingActivityId = action.activityId)
            } else {
                this
            }

        CreateTripAction.CloseAddActivityModal ->
            copy(showAddActivityModal = false, editingActivityId = null)

        is CreateTripAction.AddActivity ->
            copy(
                activities = activities + action.activity,
                showAddActivityModal = false,
                editingActivityId = null,
            )

        is CreateTripAction.UpdateActivity ->
            if (activities.any { it.id == action.activity.id }) {
                copy(
                    activities =
                        activities.map { current ->
                            if (current.id == action.activity.id) action.activity else current
                        },
                    showAddActivityModal = false,
                    editingActivityId = null,
                )
            } else {
                this
            }

        is CreateTripAction.RequestRemoveActivity ->
            if (activities.any { it.id == action.activityId }) {
                copy(pendingDeleteActivityId = action.activityId)
            } else {
                this
            }

        CreateTripAction.CancelRemoveActivity ->
            copy(pendingDeleteActivityId = null)

        is CreateTripAction.RemoveActivity -> {
            if (pendingDeleteActivityId == action.activityId) {
                val isDeletingEditedActivity = editingActivityId == action.activityId
                copy(
                    activities = activities.filterNot { it.id == action.activityId },
                    pendingDeleteActivityId = null,
                    editingActivityId = if (isDeletingEditedActivity) null else editingActivityId,
                    showAddActivityModal = showAddActivityModal && !isDeletingEditedActivity,
                )
            } else {
                this
            }
        }

        CreateTripAction.OpenAddParticipantModal ->
            copy(showAddParticipantModal = true, participantErrorMessage = null)

        CreateTripAction.CloseAddParticipantModal ->
            copy(showAddParticipantModal = false)

        is CreateTripAction.AddParticipant ->
            copy(
                participants = participants + action.participant,
                showAddParticipantModal = false,
            )

        is CreateTripAction.RemoveParticipant -> {
            val target = participants.find { it.id == action.participantId }
            if (target?.isCreator == true) {
                copy(participantErrorMessage = "O criador da viagem não pode ser excluído da viagem.")
            } else {
                withoutParticipant(action.participantId)
            }
        }

        is CreateTripAction.OpenEditParticipantModal ->
            copy(editingParticipant = action.participant, participantErrorMessage = null)

        CreateTripAction.CloseEditParticipantModal ->
            copy(editingParticipant = null)

        is CreateTripAction.UpdateParticipant -> {
            val current = participants.find { it.id == action.participant.id }
            val removingOrganizerRole = current?.isOrganizer == true && !action.participant.isOrganizer

            when {
                current?.isCreator == true && !action.participant.isOrganizer ->
                    copy(
                        participantErrorMessage =
                            "O criador da viagem sempre será o organizador e não pode ter seu papel alterado.",
                    )

                removingOrganizerRole && participants.count { it.isOrganizer } <= 1 ->
                    copy(
                        participantErrorMessage =
                            "A viagem deve ter pelo menos um Organizador. Defina outro organizador antes de alterar esta função.",
                    )

                else -> {
                    val updatedParticipant =
                        if (current?.isCreator == true) {
                            action.participant.copy(isCreator = true, isOrganizer = true, role = "Organizador")
                        } else {
                            action.participant
                        }

                    copy(
                        participants =
                            participants.map {
                                if (it.id == updatedParticipant.id) updatedParticipant else it
                            },
                        editingParticipant = null,
                        participantErrorMessage = null,
                    )
                }
            }
        }

        is CreateTripAction.OpenDeleteParticipantModal ->
            if (action.participant.isCreator) {
                copy(participantErrorMessage = "O criador da viagem não pode ser excluído da viagem.")
            } else {
                copy(deletingParticipant = action.participant, participantErrorMessage = null)
            }

        CreateTripAction.CloseDeleteParticipantModal ->
            copy(deletingParticipant = null)

        is CreateTripAction.ConfirmDeleteParticipant -> {
            val target = participants.find { it.id == action.participantId }
            when {
                target?.isCreator == true ->
                    copy(participantErrorMessage = "O criador da viagem não pode ser excluído da viagem.")

                target?.isOrganizer == true && participants.count { it.isOrganizer } <= 1 ->
                    copy(
                        participantErrorMessage =
                            "Toda viagem deve ter obrigatoriamente pelo menos um Organizador. Transfira a função antes de excluir.",
                    )

                action.hasFinancialPending ->
                    copy(
                        participantErrorMessage =
                            "Não é possível excluir viajante que possui despesas ativas vinculadas ou débitos em aberto.",
                    )

                else ->
                    withoutParticipant(action.participantId).copy(
                        deletingParticipant = null,
                        participantErrorMessage = null,
                    )
            }
        }

        CreateTripAction.DismissParticipantError ->
            copy(participantErrorMessage = null)
    }

private fun CreateTripUiState.withoutParticipant(participantId: String): CreateTripUiState =
    copy(
        participants = participants.filterNot { it.id == participantId },
        activities =
            activities.map { activity ->
                activity.copy(participantIds = activity.participantIds.filterNot { it == participantId })
            },
    )
