package com.imd.tripsplit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityRequestDto(
    val title: String,
    val dateHour: String,
    val location: String,
    val estimatedCost: Double?,
    val participantIds: List<String>,
)