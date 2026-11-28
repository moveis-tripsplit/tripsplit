package com.imd.tripsplit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponseDto(
    val id: String,
    val title: String,
    val dateHour: String,
    val location: String,
    val estimatedCost: Double? = null,
    val participantIds: List<String> = emptyList(),
)
