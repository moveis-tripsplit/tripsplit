package com.imd.tripsplit.domain.model

data class TripParticipant(
    val id: String,
    val name: String,
    val initials: String,
    val role: String = "Participante",
    val isOrganizer: Boolean = false,
    val hasAccount: Boolean = true,
    val contactOrPix: String? = null,
)
