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
    val showAddActivityModal: Boolean = false,
    val showAddParticipantModal: Boolean = false,
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

    data class OpenEditActivityModal(val activityId: String, ) : CreateTripAction

    data object CloseAddActivityModal : CreateTripAction

    data class AddActivity(val activity: TripActivity) : CreateTripAction

    data class UpdateActivity(val activity: TripActivity) : CreateTripAction

    data class RemoveActivity(val activityId: String) : CreateTripAction

    data object OpenAddParticipantModal : CreateTripAction

    data object CloseAddParticipantModal : CreateTripAction

    data class AddParticipant(val participant: TripParticipant) : CreateTripAction

    data class RemoveParticipant(val participantId: String) : CreateTripAction

    data class RequestRemoveActivity(val activityId: String, ) : CreateTripAction

    data object CancelRemoveActivity : CreateTripAction
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
        is CreateTripAction.OpenAddActivityModal -> copy(showAddActivityModal = true, editingActivityId = null)
        is CreateTripAction.OpenEditActivityModal -> if (activities.any { it.id == action.activityId }) { copy(showAddActivityModal = true, editingActivityId = action.activityId,) } else { this }
        is CreateTripAction.CloseAddActivityModal -> copy(showAddActivityModal = false, editingActivityId = null,)
        is CreateTripAction.AddActivity -> copy(activities = activities + action.activity, showAddActivityModal = false, editingActivityId = null,)
        is CreateTripAction.UpdateActivity -> if (activities.any { it.id == action.activity.id }) { copy(activities = activities.map { currentActivity -> if (currentActivity.id == action.activity.id) { action.activity } else { currentActivity } }, showAddActivityModal = false, editingActivityId = null,) } else { this }
        is CreateTripAction.RemoveActivity -> copy(activities = activities.filterNot { it.id == action.activityId })
        is CreateTripAction.OpenAddParticipantModal -> copy(showAddParticipantModal = true)
        is CreateTripAction.CloseAddParticipantModal -> copy(showAddParticipantModal = false)
        is CreateTripAction.AddParticipant -> copy(participants = participants + action.participant, showAddParticipantModal = false)
        is CreateTripAction.RemoveParticipant -> copy(participants = participants.filterNot { it.id == action.participantId }, activities = activities.map { activity -> activity.copy(participantIds = activity.participantIds.filterNot { it == action.participantId },) },)
        is CreateTripAction.RequestRemoveActivity ->
            if (activities.any { it.id == action.activityId }) {
                copy(
                    pendingDeleteActivityId = action.activityId,
                )
            } else {
                this
            }

        is CreateTripAction.CancelRemoveActivity ->
            copy(
                pendingDeleteActivityId = null,
            )

        is CreateTripAction.RemoveActivity ->
            if (pendingDeleteActivityId != action.activityId) {
                this
            } else {
                val isDeletingEditedActivity =
                    editingActivityId == action.activityId

                copy(
                    activities =
                        activities.filterNot {
                            it.id == action.activityId
                        },
                    pendingDeleteActivityId = null,
                    editingActivityId =
                        if (isDeletingEditedActivity) {
                            null
                        } else {
                            editingActivityId
                        },
                    showAddActivityModal =
                        if (isDeletingEditedActivity) {
                            false
                        } else {
                            showAddActivityModal
                        },
                )
            }
    }

