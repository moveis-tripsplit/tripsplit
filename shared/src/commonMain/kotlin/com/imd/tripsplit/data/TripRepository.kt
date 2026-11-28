package com.imd.tripsplit.data

import com.imd.tripsplit.data.remote.ParticipantApiClient
import com.imd.tripsplit.data.remote.dto.toParticipant
import com.imd.tripsplit.data.remote.dto.toRequestDto
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.User
import kotlinx.coroutines.flow.StateFlow
import com.imd.tripsplit.data.remote.ActivityApiClient
import com.imd.tripsplit.ui.screens.home.trip.TripActivity
import com.imd.tripsplit.data.remote.dto.ActivityRequestDto
import com.imd.tripsplit.data.remote.dto.ActivityResponseDto

object TripRepository {
    val trips: StateFlow<List<Trip>> = LocalDataManager.trips

    private val activityApiClient by lazy { ActivityApiClient() }

    suspend fun fetchActivities(tripId: String): Result<List<TripActivity>> =
        activityApiClient.getActivities(tripId).map { activities ->
            activities.map { it.toTripActivity() }
        }

    suspend fun createActivity(
        tripId: String,
        activity: TripActivity,
    ): Result<TripActivity> =
        activityApiClient.createActivity(tripId, activity.toActivityRequest())
            .map { it.toTripActivity(localId = activity.id) }

    suspend fun updateActivity(
        tripId: String,
        activity: TripActivity,
    ): Result<TripActivity> {
        val remoteId = activity.remoteId
            ?: return Result.failure(
                IllegalStateException("A atividade ainda não foi enviada ao servidor."),
            )

        return activityApiClient.updateActivity(tripId, remoteId, activity.toActivityRequest())
            .map { it.toTripActivity(localId = activity.id) }
    }

    suspend fun deleteActivity(
        tripId: String,
        activityId: String,
    ): Result<Unit> =
        activityApiClient.deleteActivity(tripId, activityId)
    private var _apiClient: ParticipantApiClient? = null
    var apiClient: ParticipantApiClient
        get() {
            if (_apiClient == null) {
                _apiClient =
                    try {
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

    fun updateTrip(
        updated: Trip,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Trip> = LocalDataManager.updateTrip(updated, requesterUserId, requesterEmail)

    fun getTripsForCurrentUser(): List<Trip> = LocalDataManager.getTripsForCurrentUser()

    // --- PARTICIPANTES ---

    fun findUserByEmail(email: String): User? = LocalDataManager.findUserByEmail(email)

    fun getParticipants(tripId: String): List<Participant> = LocalDataManager.getTripParticipants(tripId)

    suspend fun addParticipant(
        tripId: String,
        participant: Participant,
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Participant> {
        // 1. Persistência local imediata (Offline-First)
        val localResult =
            LocalDataManager.addParticipantToTrip(
                tripId = tripId,
                participant = participant,
                requesterUserId = requesterUserId,
                requesterEmail = requesterEmail,
            )
        if (localResult.isFailure) return localResult

        val savedParticipant = localResult.getOrThrow()

        // 2. Sincronização remota via Ktor (não bloqueia funcionamento offline se falhar)
        try {
            val remoteResult = apiClient.createParticipant(tripId, savedParticipant.toRequestDto())
            if (remoteResult.isSuccess) {
                val remoteParticipant = remoteResult.getOrNull()?.toParticipant()
                if (remoteParticipant != null) {
                    LocalDataManager.updateParticipantInTrip(
                        tripId = tripId,
                        updated = remoteParticipant,
                        requesterUserId = requesterUserId,
                        requesterEmail = requesterEmail,
                    )
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
        requesterUserId: String? = null,
        requesterEmail: String? = null,
    ): Result<Participant> {
        val localResult =
            LocalDataManager.updateParticipantInTrip(
                tripId = tripId,
                updated = participant,
                requesterUserId = requesterUserId,
                requesterEmail = requesterEmail,
            )
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
        requesterUserId: String? = null,
        requesterEmail: String? = null,
        hasFinancialPending: Boolean = false,
    ): Result<Unit> {
        val localResult =
            LocalDataManager.removeParticipantFromTrip(
                tripId = tripId,
                participantId = participantId,
                requesterUserId = requesterUserId,
                requesterEmail = requesterEmail,
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
    suspend fun syncPendingActivities(
        tripId: String,
        requesterUserId: String?,
        requesterEmail: String?,
    ): Result<Unit> {
        fun currentTrip(): Trip =
            getTrip(tripId) ?: error("Viagem não encontrada.")

        fun persist(trip: Trip) {
            updateTrip(trip, requesterUserId, requesterEmail).getOrThrow()
        }

        return try {
            for (localId in currentTrip().pendingActivityUpsertIds) {
                val activity = currentTrip().activities.first { it.id == localId }

                val confirmed = if (activity.remoteId == null) {
                    createActivity(tripId, activity).getOrThrow()
                } else {
                    updateActivity(tripId, activity).getOrThrow()
                }

                val latest = currentTrip()
                persist(
                    latest.copy(
                        activities = latest.activities.map {
                            if (it.id == localId) it.copy(remoteId = confirmed.remoteId) else it
                        },
                        pendingActivityUpsertIds =
                            latest.pendingActivityUpsertIds.filterNot { it == localId },
                    ),
                )
            }

            for (remoteId in currentTrip().pendingActivityDeleteIds) {
                deleteActivity(tripId, remoteId).getOrThrow()

                val latest = currentTrip()
                persist(
                    latest.copy(
                        pendingActivityDeleteIds =
                            latest.pendingActivityDeleteIds.filterNot { it == remoteId },
                    ),
                )
            }

            Result.success(Unit)
        } catch (cancelled: kotlinx.coroutines.CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

}

private fun ActivityResponseDto.toTripActivity(localId: String = this.id): TripActivity =
    TripActivity(
        id = localId,
        title = title,
        dateHour = dateHour,
        location = location,
        estimatedCost = estimatedCost,
        participantIds = participantIds,
        remoteId = this.id,
    )
private fun TripActivity.toActivityRequest(): ActivityRequestDto =
    ActivityRequestDto(
        title = title,
        dateHour = dateHour,
        location = location,
        estimatedCost = estimatedCost,
        participantIds = participantIds,
    )
