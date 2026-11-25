package com.imd.tripsplit.data

import com.imd.tripsplit.model.Trip
import kotlinx.coroutines.flow.StateFlow

object TripRepository {
    val trips: StateFlow<List<Trip>> = LocalDataManager.trips

    fun addTrip(trip: Trip) {
        LocalDataManager.addTrip(trip)
    }

    fun getTrip(id: String): Trip? = LocalDataManager.getTrip(id)

    fun updateTrip(updated: Trip) {
        LocalDataManager.updateTrip(updated)
    }

    fun getTripsForCurrentUser(): List<Trip> = LocalDataManager.getTripsForCurrentUser()
}
