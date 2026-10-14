package com.imd.tripsplit.ui.screens.trip

/**
 * Representa uma atividade no roteiro da viagem.
 * Observação: O usuário pode criar a viagem sem atividades e adicioná-las posteriormente.
 */
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
)
