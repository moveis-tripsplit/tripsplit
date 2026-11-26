package com.imd.tripsplit.domain.model

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
