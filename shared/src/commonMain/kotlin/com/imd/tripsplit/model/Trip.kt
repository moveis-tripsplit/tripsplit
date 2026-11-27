package com.imd.tripsplit.model

import com.imd.tripsplit.data.toTripParticipant
import com.imd.tripsplit.ui.screens.home.trip.CreateTripUiState
import com.imd.tripsplit.ui.screens.home.trip.TripActivity
import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: String,
    val userId: String = "",
    val title: String,
    val destination: String,
    val status: TripStatus,
    val totalAmount: Double,
    val myBalance: Double,
    val startDate: String,
    val endDate: String,
    val description: String = "",
    val participants: List<Participant> = emptyList(),
    val activities: List<TripActivity> = emptyList(),
) {
    fun periodLabel(): String {
        return "$startDate - $endDate"
    }

    fun isCreator(participant: Participant): Boolean =
        participant.isCreator || (userId.isNotBlank() && (participant.userId == userId || participant.id == userId))
}

fun Trip.toUiState(): CreateTripUiState =
    CreateTripUiState(
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        activities = activities,
        participants =
            participants.map { p ->
                val isThisCreator = isCreator(p)
                p.toTripParticipant().copy(
                    isCreator = isThisCreator,
                    isOrganizer = if (isThisCreator) true else p.isOrganizer,
                    role = if (isThisCreator) "Organizador" else p.role,
                )
            },
    )
