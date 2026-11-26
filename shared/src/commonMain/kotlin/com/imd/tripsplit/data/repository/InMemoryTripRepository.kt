package com.imd.tripsplit.data.repository

import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

class InMemoryTripRepository : TripRepository {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())

    override fun observeTrips(): Flow<List<Trip>> = _trips.asStateFlow()

    override suspend fun getTrip(id: String): Trip? = _trips.value.find { it.id == id }

    override suspend fun addTrip(trip: Trip) {
        _trips.update { trips -> listOf(trip) + trips }
    }

    override suspend fun updateTrip(trip: Trip) {
        _trips.update { trips -> trips.map { if (it.id == trip.id) trip else it } }
    }
}
