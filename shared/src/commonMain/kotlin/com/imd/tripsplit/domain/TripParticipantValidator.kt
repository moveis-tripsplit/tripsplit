package com.imd.tripsplit.domain

import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.Trip

object TripParticipantValidator {
    /**
     * Regra 1: O criador da viagem não pode ser excluído da viagem.
     * Regra 2: Toda viagem deve ter obrigatoriamente pelo menos um Organizador.
     * O organizador não pode ser excluído sem transferir a função antes.
     * Regra 3: Validação de pendências financeiras (despesas ativas ou débitos).
     * Regra 4: Organizador é o único que pode excluir participantes.
     *          Quem é participante não pode excluir outros participantes.
     */
    fun validateDeletion(
        trip: Trip,
        participantId: String,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
        hasFinancialPending: Boolean = false,
    ): Result<Unit> {
        val target = trip.participants.find { it.id == participantId }
            ?: return Result.failure(IllegalArgumentException("Viajante não encontrado na viagem."))

        // Regra: Organizador é o único que pode excluir participantes
        if (requesterUserId != null || requesterEmail != null) {
            val isRequesterOrganizer = trip.isUserOrganizer(requesterUserId, requesterEmail)
            if (!isRequesterOrganizer) {
                return Result.failure(
                    IllegalStateException("Apenas organizadores podem excluir participantes da viagem.")
                )
            }
        }

        // Regra: O criador da viagem não pode ser excluído
        if (trip.isCreator(target)) {
            return Result.failure(
                IllegalStateException("O criador da viagem não pode ser excluído da viagem.")
            )
        }

        // Regra: Validação de organizador mínimo
        if (target.isOrganizer) {
            val totalOrganizers = trip.participants.count { it.isOrganizer }
            if (totalOrganizers <= 1) {
                return Result.failure(
                    IllegalStateException(
                        "Toda viagem deve ter obrigatoriamente pelo menos um Organizador. Transfira a função antes de excluir."
                    )
                )
            }
        }

        // Regra: Validação de pendências financeiras (despesas ativas ou débitos)
        if (hasFinancialPending) {
            return Result.failure(
                IllegalStateException(
                    "Não é possível excluir viajante que possui despesas ativas vinculadas ou débitos em aberto."
                )
            )
        }

        return Result.success(Unit)
    }

    /**
     * Regra 1: O criador da viagem sempre será o organizador e não pode ter seu papel alterado.
     * Regra 2: Se for o único organizador, não pode alterar papel para participante.
     * Regra 3: O criador da viagem é o único que pode definir algum participante da viagem como organizador.
     */
    fun validateRoleChange(
        trip: Trip,
        participantId: String,
        newRoleIsOrganizer: Boolean,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Unit> {
        val target = trip.participants.find { it.id == participantId }
            ?: return Result.failure(IllegalArgumentException("Viajante não encontrado na viagem."))

        // Regra: O criador da viagem sempre será o organizador
        if (trip.isCreator(target) && !newRoleIsOrganizer) {
            return Result.failure(
                IllegalStateException("O criador da viagem sempre será o organizador e não pode ter seu papel alterado.")
            )
        }

        // Regra: O criador da viagem é o único que pode definir algum participante como organizador
        if (newRoleIsOrganizer && !target.isOrganizer) {
            if (requesterUserId != null || requesterEmail != null) {
                if (!trip.isUserCreator(requesterUserId, requesterEmail)) {
                    return Result.failure(
                        IllegalStateException("Apenas o criador da viagem pode definir participantes como organizador.")
                    )
                }
            }
        }

        if (target.isOrganizer && !newRoleIsOrganizer) {
            val totalOrganizers = trip.participants.count { it.isOrganizer }
            if (totalOrganizers <= 1) {
                return Result.failure(
                    IllegalStateException(
                        "A viagem deve ter pelo menos um Organizador. Defina outro organizador antes de alterar esta função."
                    )
                )
            }
        }

        return Result.success(Unit)
    }

    /**
     * Regra: Organizador é o único que pode editar participantes.
     * Participante não tem permissão para editar participantes.
     */
    fun validateEdition(
        trip: Trip,
        targetParticipantId: String,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Unit> {
        val target = trip.participants.find { it.id == targetParticipantId }
            ?: return Result.failure(IllegalArgumentException("Viajante não encontrado na viagem."))

        if (requesterUserId != null || requesterEmail != null) {
            val isRequesterOrganizer = trip.isUserOrganizer(requesterUserId, requesterEmail)
            if (!isRequesterOrganizer) {
                return Result.failure(
                    IllegalStateException("Participantes não têm permissão para editar participantes. Apenas organizadores têm essa permissão.")
                )
            }
        }

        return Result.success(Unit)
    }

    /**
     * Regra: Apenas o criador da viagem pode adicionar participantes e definir algum participante como organizador.
     * Organizador não pode adicionar novos participantes na viagem.
     * Participante não pode adicionar novos participantes na viagem.
     */
    fun validateAddition(
        trip: Trip,
        newParticipant: Participant,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Unit> {
        if (requesterUserId != null || requesterEmail != null) {
            val isRequesterCreator = trip.isUserCreator(requesterUserId, requesterEmail)
            if (!isRequesterCreator) {
                return Result.failure(
                    IllegalStateException("Apenas o criador da viagem pode adicionar participantes.")
                )
            }

            if (newParticipant.isOrganizer && !isRequesterCreator) {
                return Result.failure(
                    IllegalStateException("Apenas o criador da viagem pode definir participantes como organizador.")
                )
            }
        }

        return Result.success(Unit)
    }

    /**
     * Regra 3: Validação de dados cadastrais para adicionar/editar.
     * Usuários sem conta precisam de nome válido (mínimo 2 caracteres).
     * Usuários com conta precisam de e-mail válido.
     */
    fun validateParticipantData(
        name: String,
        email: String?,
        hasAccount: Boolean,
    ): Result<Unit> {
        val trimmedName = name.trim()
        if (trimmedName.length < 2) {
            return Result.failure(IllegalArgumentException("O nome do viajante deve ter pelo menos 2 caracteres."))
        }

        if (hasAccount) {
            val trimmedEmail = email?.trim().orEmpty()
            if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
                return Result.failure(IllegalArgumentException("Informe um e-mail válido para vincular a conta do usuário."))
            }
        }

        return Result.success(Unit)
    }
}
