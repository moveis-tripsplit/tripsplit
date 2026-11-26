package com.imd.tripsplit.domain.model

data class TripDraft(
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val participants: List<TripParticipant>,
    val activities: List<TripActivity>,
)
