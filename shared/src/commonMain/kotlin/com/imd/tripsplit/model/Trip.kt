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
    val pendingActivityUpsertIds: List<String> = emptyList(),
    val pendingActivityDeleteIds: List<String> = emptyList(),
) {
    fun periodLabel(): String {
        return "$startDate - $endDate"
    }

    fun isCreator(participant: Participant): Boolean = participant.isCreator || (userId.isNotBlank() && (participant.userId == userId || participant.id == userId))

    fun isUserCreator(
        checkUserId: String?,
        checkEmail: String? = null
    ): Boolean {
        if (!checkUserId.isNullOrBlank() && userId.isNotBlank() && userId == checkUserId) return true
        val p =
            participants.find {
                (!checkUserId.isNullOrBlank() && (it.userId == checkUserId || it.id == checkUserId)) ||
                    (!checkEmail.isNullOrBlank() && !it.email.isNullOrBlank() && it.email.trim().equals(checkEmail.trim(), ignoreCase = true))
            }
        return p?.let { isCreator(it) } == true
    }

    fun isUserOrganizer(
        checkUserId: String?,
        checkEmail: String? = null
    ): Boolean {
        if (isUserCreator(checkUserId, checkEmail)) return true
        val p =
            participants.find {
                (!checkUserId.isNullOrBlank() && (it.userId == checkUserId || it.id == checkUserId)) ||
                    (!checkEmail.isNullOrBlank() && !it.email.isNullOrBlank() && it.email.trim().equals(checkEmail.trim(), ignoreCase = true))
            }
        return p?.isOrganizer == true || (p != null && (p.isCreator || isCreator(p)))
    }
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
