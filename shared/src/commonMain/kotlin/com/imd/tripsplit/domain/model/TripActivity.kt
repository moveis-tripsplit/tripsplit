package com.imd.tripsplit.domain.model

data class TripActivity(
    val id: String,
    val title: String,
    val dateHour: String,
    val location: String,
    val estimatedCost: Double? = null,
)
