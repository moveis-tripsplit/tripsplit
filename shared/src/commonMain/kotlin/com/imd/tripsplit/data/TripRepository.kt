package com.imd.tripsplit.data

import com.imd.tripsplit.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TripRepository {
    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    fun addTrip(trip: Trip) {
        _trips.update { listOf(trip) + it }
    }

    fun getTrip(id: String): Trip? = _trips.value.find { it.id == id }

    fun updateTrip(updated: Trip) {
        _trips.update { list -> list.map { if (it.id == updated.id) updated else it } }
    }
}
