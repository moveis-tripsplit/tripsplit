package com.imd.tripsplit.model

data class Trip(
    val id: String,
    val title: String,
    val destination: String,
    val status: TripStatus,
    val totalAmount: Double,
    val myBalance: Double,
    val startDate: String,
    val endDate: String,
    val participants: List<Participant> = emptyList()
) {
    fun periodLabel(): String {
        return "$startDate - $endDate"
    }
}