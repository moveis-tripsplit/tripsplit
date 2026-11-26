package com.imd.tripsplit.domain.model

data class Participant(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val role: String = "Participante",
    val isOrganizer: Boolean = false,
    val contactOrPix: String? = null,
)
