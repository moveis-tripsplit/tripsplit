package com.imd.tripsplit.domain.usecase

import com.imd.tripsplit.domain.model.Participant
import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.model.TripDraft
import com.imd.tripsplit.domain.repository.TripRepository

class UpdateTripUseCase(
    private val repository: TripRepository,
) {
    suspend operator fun invoke(existingTrip: Trip, draft: TripDraft): Trip {
        val updatedTrip =
            existingTrip.copy(
                title = draft.title.trim(),
                description = draft.description.trim(),
                startDate = draft.startDate,
                endDate = draft.endDate,
                participants = draft.participants.map {
                    Participant(
                        id = it.id,
                        name = it.name,
                        role = it.role,
                        isOrganizer = it.isOrganizer,
                        contactOrPix = it.contactOrPix,
                    )
                },
                activities = draft.activities,
            )
        repository.updateTrip(updatedTrip)
        return updatedTrip
    }
}
