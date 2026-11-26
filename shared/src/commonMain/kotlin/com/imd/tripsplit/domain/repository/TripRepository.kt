package com.imd.tripsplit.domain.repository

import com.imd.tripsplit.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun observeTrips(): Flow<List<Trip>>

    suspend fun getTrip(id: String): Trip?

    suspend fun addTrip(trip: Trip)

    suspend fun updateTrip(trip: Trip)
}
