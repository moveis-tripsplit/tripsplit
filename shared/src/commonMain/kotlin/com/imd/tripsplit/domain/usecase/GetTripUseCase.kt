package com.imd.tripsplit.domain.usecase

import com.imd.tripsplit.domain.repository.TripRepository

class GetTripUseCase(
    private val repository: TripRepository,
) {
    suspend operator fun invoke(id: String) = repository.getTrip(id)
}
