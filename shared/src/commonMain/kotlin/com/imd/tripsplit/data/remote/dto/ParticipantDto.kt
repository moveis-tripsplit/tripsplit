package com.imd.tripsplit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantRequestDto(
    val name: String,
    val email: String? = null,
    val role: String = "Participante",
    val contactOrPix: String? = null,
    val hasAccount: Boolean = false,
    val userId: String? = null,
)

@Serializable
data class ParticipantResponseDto(
    val id: String,
    val tripId: String,
    val name: String,
    val email: String? = null,
    val role: String = "Participante",
    val contactOrPix: String? = null,
    val hasAccount: Boolean = false,
    val userId: String? = null,
    val avatarUrl: String? = null,
)

fun ParticipantResponseDto.toParticipant(): com.imd.tripsplit.model.Participant =
    com.imd.tripsplit.model.Participant(
        id = id,
        name = name,
        email = email,
        avatarUrl = avatarUrl,
        role = role,
        isOrganizer = role.equals("Organizador", ignoreCase = true),
        contactOrPix = contactOrPix,
        hasAccount = hasAccount,
        userId = userId,
    )

fun com.imd.tripsplit.model.Participant.toRequestDto(): ParticipantRequestDto =
    ParticipantRequestDto(
        name = name,
        email = email,
        role = role,
        contactOrPix = contactOrPix,
        hasAccount = hasAccount,
        userId = userId,
    )
