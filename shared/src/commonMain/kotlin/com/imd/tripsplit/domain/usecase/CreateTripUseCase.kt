package com.imd.tripsplit.domain.usecase

import com.imd.tripsplit.domain.model.Participant
import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.model.TripDraft
import com.imd.tripsplit.domain.model.TripStatus
import com.imd.tripsplit.domain.repository.TripRepository
import kotlin.random.Random

class CreateTripUseCase(
    private val repository: TripRepository,
) {
    suspend operator fun invoke(draft: TripDraft): Trip {
        val trip =
            Trip(
                id = "trip-${Random.nextInt(1000, 9999)}",
                title = draft.title.trim(),
                description = draft.description.trim(),
                destination = draft.title.trim(),
                status = TripStatus.UPCOMING,
                totalAmount = draft.activities.sumOf { it.estimatedCost ?: 0.0 },
                myBalance = 0.0,
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
        repository.addTrip(trip)
        return trip
    }
}
