package com.imd.tripsplit.data

import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant

fun TripParticipant.toParticipant(): Participant =
    Participant(
        id = id,
        name = name,
        email = email,
        role = if (isCreator) "Organizador" else role,
        isOrganizer = if (isCreator) true else isOrganizer,
        contactOrPix = contactOrPix,
        hasAccount = hasAccount,
        userId = userId,
        isCreator = isCreator,
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
        role = if (isCreator) "Organizador" else role,
        isOrganizer = if (isCreator) true else isOrganizer,
        hasAccount = hasAccount,
        contactOrPix = contactOrPix,
        email = email,
        userId = userId,
        isCreator = isCreator,
    )
