package com.imd.tripsplit.data

import com.imd.tripsplit.data.remote.ParticipantApiClient
import com.imd.tripsplit.data.remote.dto.toParticipant
import com.imd.tripsplit.data.remote.dto.toRequestDto
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.User
import kotlinx.coroutines.flow.StateFlow

object TripRepository {
    val trips: StateFlow<List<Trip>> = LocalDataManager.trips

    private var _apiClient: ParticipantApiClient? = null
    var apiClient: ParticipantApiClient
        get() {
            if (_apiClient == null) {
                _apiClient = try {
                    ParticipantApiClient()
                } catch (e: Exception) {
                    e.printStackTrace()
                    ParticipantApiClient()
                }
            }
            return _apiClient!!
        }
        set(value) {
            _apiClient = value
        }

    fun addTrip(trip: Trip) {
        LocalDataManager.addTrip(trip)
    }

    fun getTrip(id: String): Trip? = LocalDataManager.getTrip(id)

    fun updateTrip(updated: Trip) {
        LocalDataManager.updateTrip(updated)
    }

    fun getTripsForCurrentUser(): List<Trip> = LocalDataManager.getTripsForCurrentUser()

    // --- PARTICIPANTES ---

    fun findUserByEmail(email: String): User? = LocalDataManager.findUserByEmail(email)

    fun getParticipants(tripId: String): List<Participant> = LocalDataManager.getTripParticipants(tripId)

    suspend fun addParticipant(
        tripId: String,
        participant: Participant,
    ): Result<Participant> {
        // 1. Persistência local imediata (Offline-First)
        val localResult = LocalDataManager.addParticipantToTrip(tripId, participant)
        if (localResult.isFailure) return localResult

        val savedParticipant = localResult.getOrThrow()

        // 2. Sincronização remota via Ktor (não bloqueia funcionamento offline se falhar)
        try {
            val remoteResult = apiClient.createParticipant(tripId, savedParticipant.toRequestDto())
            if (remoteResult.isSuccess) {
                val remoteParticipant = remoteResult.getOrNull()?.toParticipant()
                if (remoteParticipant != null) {
                    LocalDataManager.updateParticipantInTrip(tripId, remoteParticipant)
                }
            }
        } catch (_: Exception) {
            // Falha de rede tolerada: dados continuam persistidos localmente
        }

        return Result.success(savedParticipant)
    }

    suspend fun updateParticipant(
        tripId: String,
        participant: Participant,
    ): Result<Participant> {
        val localResult = LocalDataManager.updateParticipantInTrip(tripId, participant)
        if (localResult.isFailure) return localResult

        try {
            apiClient.updateParticipant(tripId, participant.id, participant.toRequestDto())
        } catch (_: Exception) {
            // Falha de rede tolerada
        }

        return localResult
    }

    suspend fun removeParticipant(
        tripId: String,
        participantId: String,
        hasFinancialPending: Boolean = false,
    ): Result<Unit> {
        val localResult = LocalDataManager.removeParticipantFromTrip(
            tripId = tripId,
            participantId = participantId,
            hasFinancialPending = hasFinancialPending,
        )
        if (localResult.isFailure) return localResult

        try {
            apiClient.deleteParticipant(tripId, participantId)
        } catch (_: Exception) {
            // Falha de rede tolerada
        }

        return localResult
    }

    suspend fun syncParticipants(tripId: String): Result<List<Participant>> {
        return try {
            val remoteResult = apiClient.getParticipants(tripId)
            if (remoteResult.isSuccess) {
                val remoteList = remoteResult.getOrThrow().map { it.toParticipant() }
                val trip = getTrip(tripId)
                if (trip != null) {
                    updateTrip(trip.copy(participants = remoteList))
                }
                Result.success(remoteList)
            } else {
                Result.success(getParticipants(tripId))
            }
        } catch (_: Exception) {
            Result.success(getParticipants(tripId))
        }
    }
}
