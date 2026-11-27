package com.imd.tripsplit.data

import com.imd.tripsplit.data.storage.AppDatabaseData
import com.imd.tripsplit.data.storage.PlatformStorage
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

object LocalDataManager {
    private const val DB_FILE_NAME = "tripsplit_db.json"
    private val storage = PlatformStorage()
    private val json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }

    private val _dbState = MutableStateFlow(loadInitialData())

    val users: StateFlow<List<User>>
        get() = MutableStateFlow(_dbState.value.users)

    private val _currentUser = MutableStateFlow<User?>(_dbState.value.users.find { it.id == _dbState.value.currentUserId })
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _trips = MutableStateFlow<List<Trip>>(_dbState.value.trips)
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    private fun loadInitialData(): AppDatabaseData {
        val raw = storage.readString(DB_FILE_NAME) ?: return AppDatabaseData()
        return try {
            json.decodeFromString<AppDatabaseData>(raw)
        } catch (e: Exception) {
            e.printStackTrace()
            AppDatabaseData()
        }
    }

    private fun persist() {
        val current = _dbState.value
        try {
            val encoded = json.encodeToString(AppDatabaseData.serializer(), current)
            storage.writeString(DB_FILE_NAME, encoded)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- USUÁRIOS & AUTENTICAÇÃO ---

    fun registerUser(user: User): Result<User> {
        val current = _dbState.value
        if (current.users.any { it.email.equals(user.email.trim(), ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("Já existe um usuário cadastrado com este e-mail."))
        }

        val updatedUsers = current.users + user
        _dbState.update { it.copy(users = updatedUsers, currentUserId = user.id) }
        _currentUser.value = user
        persist()
        return Result.success(user)
    }

    fun login(
        email: String,
        password: String
    ): Result<User> {
        val found =
            _dbState.value.users.find {
                it.email.equals(email.trim(), ignoreCase = true) && it.password == password
            }

        return if (found != null) {
            _dbState.update { it.copy(currentUserId = found.id) }
            _currentUser.value = found
            persist()
            Result.success(found)
        } else {
            Result.failure(IllegalArgumentException("E-mail ou senha incorretos."))
        }
    }

    fun logout() {
        _dbState.update { it.copy(currentUserId = null) }
        _currentUser.value = null
        persist()
    }

    // --- VIAGENS ---

    fun addTrip(trip: Trip) {
        val loggedId = _currentUser.value?.id ?: ""
        val tripUserId = if (trip.userId.isBlank()) loggedId else trip.userId

        val updatedParticipants =
            trip.participants.map { p ->
                val isCreator = p.isCreator || (tripUserId.isNotBlank() && (p.userId == tripUserId || p.id == tripUserId))
                if (isCreator) {
                    p.copy(isOrganizer = true, role = "Organizador", isCreator = true)
                } else {
                    p
                }
            }

        val tripWithUser = trip.copy(userId = tripUserId, participants = updatedParticipants)

        _dbState.update { current ->
            current.copy(trips = listOf(tripWithUser) + current.trips)
        }
        _trips.value = _dbState.value.trips
        persist()
    }

    fun getTrip(id: String): Trip? = _trips.value.find { it.id == id }

    private fun internalPersistTrip(finalUpdated: Trip) {
        val updatedParticipants =
            finalUpdated.participants.map { p ->
                val isCreator = finalUpdated.isCreator(p)
                if (isCreator) {
                    p.copy(isOrganizer = true, role = "Organizador", isCreator = true)
                } else {
                    p
                }
            }
        val safeTrip = finalUpdated.copy(participants = updatedParticipants)
        _dbState.update { current ->
            current.copy(trips = current.trips.map { if (it.id == safeTrip.id) safeTrip else it })
        }
        _trips.value = _dbState.value.trips
        persist()
    }

    fun updateTrip(
        updated: Trip,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Trip> {
        val existingTrip = getTrip(updated.id)
        if (existingTrip != null) {
            val effectiveRequesterId = requesterUserId ?: _currentUser.value?.id
            val effectiveRequesterEmail = requesterEmail ?: _currentUser.value?.email
            if ((effectiveRequesterId != null || effectiveRequesterEmail != null) &&
                !existingTrip.isUserCreator(effectiveRequesterId, effectiveRequesterEmail)
            ) {
                return Result.failure(
                    IllegalStateException("Apenas o criador pode editar a viagem que criou.")
                )
            }
        }

        internalPersistTrip(updated)
        return Result.success(getTrip(updated.id) ?: updated)
    }

    fun getTripsForCurrentUser(): List<Trip> {
        val user = _currentUser.value
        val userId = user?.id
        val userEmail = user?.email?.trim()
        return if (userId != null) {
            _trips.value.filter { trip ->
                trip.userId == userId ||
                    trip.userId.isBlank() ||
                    trip.participants.any { p ->
                        (p.userId != null && p.userId == userId) ||
                            (!p.email.isNullOrBlank() && userEmail != null && p.email.trim().equals(userEmail, ignoreCase = true))
                    }
            }
        } else {
            _trips.value
        }
    }

    // --- PARTICIPANTES / VIAJANTES ---

    fun findUserByEmail(email: String): User? {
        val query = email.trim()
        return _dbState.value.users.find { it.email.equals(query, ignoreCase = true) }
    }

    fun getTripParticipants(tripId: String): List<com.imd.tripsplit.model.Participant> {
        return getTrip(tripId)?.participants ?: emptyList()
    }

    fun addParticipantToTrip(
        tripId: String,
        participant: com.imd.tripsplit.model.Participant,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<com.imd.tripsplit.model.Participant> {
        val trip = getTrip(tripId) ?: return Result.failure(IllegalArgumentException("Viagem não encontrada."))

        val effectiveRequesterId = requesterUserId ?: _currentUser.value?.id
        val effectiveRequesterEmail = requesterEmail ?: _currentUser.value?.email

        val authValidation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateAddition(
                trip = trip,
                newParticipant = participant,
                requesterUserId = effectiveRequesterId,
                requesterEmail = effectiveRequesterEmail,
            )
        if (authValidation.isFailure) {
            return Result.failure(authValidation.exceptionOrNull() ?: IllegalStateException("Erro ao adicionar participante."))
        }

        // Validação de dados cadastrais
        val dataValidation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateParticipantData(
                name = participant.name,
                email = participant.email,
                hasAccount = participant.hasAccount,
            )
        if (dataValidation.isFailure) {
            return Result.failure(dataValidation.exceptionOrNull() ?: IllegalArgumentException("Dados inválidos."))
        }

        // Se tiver conta e userId não foi definido, tenta vincular com usuário local
        val resolvedParticipant =
            if (participant.hasAccount && participant.userId.isNullOrBlank()) {
                val matchedUser = participant.email?.let { findUserByEmail(it) }
                if (matchedUser != null) {
                    participant.copy(userId = matchedUser.id, name = if (participant.name.isBlank()) matchedUser.name else participant.name)
                } else {
                    participant
                }
            } else {
                participant
            }

        val updatedParticipants = trip.participants + resolvedParticipant
        internalPersistTrip(trip.copy(participants = updatedParticipants))
        return Result.success(resolvedParticipant)
    }

    fun updateParticipantInTrip(
        tripId: String,
        updated: com.imd.tripsplit.model.Participant,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<com.imd.tripsplit.model.Participant> {
        val trip = getTrip(tripId) ?: return Result.failure(IllegalArgumentException("Viagem não encontrada."))

        val effectiveRequesterId = requesterUserId ?: _currentUser.value?.id
        val effectiveRequesterEmail = requesterEmail ?: _currentUser.value?.email

        val editValidation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateEdition(
                trip = trip,
                targetParticipantId = updated.id,
                requesterUserId = effectiveRequesterId,
                requesterEmail = effectiveRequesterEmail,
            )
        if (editValidation.isFailure) {
            return Result.failure(editValidation.exceptionOrNull() ?: IllegalStateException("Erro ao editar participante."))
        }

        // Validação de dados cadastrais
        val dataValidation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateParticipantData(
                name = updated.name,
                email = updated.email,
                hasAccount = updated.hasAccount,
            )
        if (dataValidation.isFailure) {
            return Result.failure(dataValidation.exceptionOrNull() ?: IllegalArgumentException("Dados inválidos."))
        }

        // Validação de alteração de papel caso deixe de ser organizador ou seja promovido
        val roleValidation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateRoleChange(
                trip = trip,
                participantId = updated.id,
                newRoleIsOrganizer = updated.isOrganizer,
                requesterUserId = effectiveRequesterId,
                requesterEmail = effectiveRequesterEmail,
            )
        if (roleValidation.isFailure) {
            return Result.failure(roleValidation.exceptionOrNull() ?: IllegalStateException("Erro ao alterar papel."))
        }

        val updatedParticipants = trip.participants.map { if (it.id == updated.id) updated else it }
        internalPersistTrip(trip.copy(participants = updatedParticipants))
        return Result.success(updated)
    }

    fun removeParticipantFromTrip(
        tripId: String,
        participantId: String,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
        hasFinancialPending: Boolean = false,
    ): Result<Unit> {
        val trip = getTrip(tripId) ?: return Result.failure(IllegalArgumentException("Viagem não encontrada."))

        val effectiveRequesterId = requesterUserId ?: _currentUser.value?.id
        val effectiveRequesterEmail = requesterEmail ?: _currentUser.value?.email

        val validation =
            com.imd.tripsplit.domain.TripParticipantValidator.validateDeletion(
                trip = trip,
                participantId = participantId,
                requesterUserId = effectiveRequesterId,
                requesterEmail = effectiveRequesterEmail,
                hasFinancialPending = hasFinancialPending,
            )
        if (validation.isFailure) {
            return Result.failure(validation.exceptionOrNull() ?: IllegalStateException("Erro ao remover participante."))
        }

        val updatedParticipants = trip.participants.filterNot { it.id == participantId }
        internalPersistTrip(trip.copy(participants = updatedParticipants))
        return Result.success(Unit)
    }
}
