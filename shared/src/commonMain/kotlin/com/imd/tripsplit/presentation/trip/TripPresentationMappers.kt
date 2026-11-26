package com.imd.tripsplit.presentation.trip

import com.imd.tripsplit.domain.model.Participant
import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.model.TripDraft
import com.imd.tripsplit.domain.model.TripParticipant

fun CreateTripUiState.toTripDraft(): TripDraft =
    TripDraft(
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        participants = participants,
        activities = activities,
    )

fun Trip.toCreateTripUiState(): CreateTripUiState =
    CreateTripUiState(
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        activities = activities,
        participants = participants.map(Participant::toTripParticipant),
        isEditing = true,
    )

private fun Participant.toTripParticipant(): TripParticipant =
    TripParticipant(
        id = id,
        name = name,
        initials = name.trim().split(" ").filter { it.isNotBlank() }.map { it.first().uppercase() }.take(2).joinToString("").ifEmpty { "P" },
        role = role,
        isOrganizer = isOrganizer,
        hasAccount = true,
        contactOrPix = contactOrPix,
    )
