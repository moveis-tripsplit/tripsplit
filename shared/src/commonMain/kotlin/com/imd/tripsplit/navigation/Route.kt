package com.imd.tripsplit.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route

    @Serializable
    data class TripDetail(val tripId: String) : Route

    @Serializable
    data object NewTrip : Route
}
