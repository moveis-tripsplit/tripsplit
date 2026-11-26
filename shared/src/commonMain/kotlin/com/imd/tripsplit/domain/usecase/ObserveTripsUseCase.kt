package com.imd.tripsplit.domain.usecase

import com.imd.tripsplit.domain.repository.TripRepository

class ObserveTripsUseCase(
    private val repository: TripRepository,
) {
    operator fun invoke() = repository.observeTrips()
}
