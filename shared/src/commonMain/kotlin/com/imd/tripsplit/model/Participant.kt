package com.imd.tripsplit.model

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null,
    val role: String = "Participante",
    val isOrganizer: Boolean = false,
    val contactOrPix: String? = null,
    val hasAccount: Boolean = false,
    val userId: String? = null,
)
