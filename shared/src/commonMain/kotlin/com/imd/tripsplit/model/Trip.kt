package com.imd.tripsplit.model

import com.imd.tripsplit.data.toTripParticipant
import com.imd.tripsplit.ui.screens.trip.CreateTripUiState
import com.imd.tripsplit.ui.screens.trip.TripActivity

data class Trip(
    val id: String,
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
}

fun Trip.toUiState(): CreateTripUiState =
    CreateTripUiState(
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        activities = activities,
        participants = participants.map { it.toTripParticipant() },
    )
