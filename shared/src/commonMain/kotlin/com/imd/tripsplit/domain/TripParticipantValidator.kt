package com.imd.tripsplit.domain

import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.Trip

object TripParticipantValidator {
    /**
     * Regra 1: Toda viagem deve ter obrigatoriamente pelo menos um Organizador.
     * O organizador não pode ser excluído sem transferir a função antes.
     */
    fun validateDeletion(
        trip: Trip,
        participantId: String,
        hasFinancialPending: Boolean = false,
    ): Result<Unit> {
        val target = trip.participants.find { it.id == participantId }
            ?: return Result.failure(IllegalArgumentException("Viajante não encontrado na viagem."))

        // Regra 1: Validação de organizador mínimo
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

        // Regra 2: Validação de pendências financeiras (despesas ativas ou débitos)
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
     * Regra 1 (ao editar papel): Se for o único organizador, não pode alterar papel para participante.
     */
    fun validateRoleChange(
        trip: Trip,
        participantId: String,
        newRoleIsOrganizer: Boolean,
    ): Result<Unit> {
        val target = trip.participants.find { it.id == participantId }
            ?: return Result.failure(IllegalArgumentException("Viajante não encontrado na viagem."))

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
