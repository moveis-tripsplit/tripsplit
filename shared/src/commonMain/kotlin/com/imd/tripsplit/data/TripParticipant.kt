package com.imd.tripsplit.data

import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant

fun TripParticipant.toParticipant(): Participant =
    Participant(
        id = id,
        name = name,
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
        role = "Participante",
        isOrganizer = false,
        hasAccount = true,
        contactOrPix = null,
    )
