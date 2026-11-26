package com.imd.tripsplit.data

import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant

fun TripParticipant.toParticipant(): Participant =
    Participant(
        id = id,
        name = name,
        email = email,
        role = role,
        isOrganizer = isOrganizer,
        contactOrPix = contactOrPix,
        hasAccount = hasAccount,
        userId = userId,
    )

fun Participant.toTripParticipant(): TripParticipant =
    TripParticipant(
        id = id,
        name = name,
        initials =
            name
                .trim()
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.first().uppercase() }
                .take(2)
                .joinToString("")
                .ifEmpty { "P" },
        role = role,
        isOrganizer = isOrganizer,
        hasAccount = hasAccount,
        contactOrPix = contactOrPix,
        email = email,
        userId = userId,
    )
