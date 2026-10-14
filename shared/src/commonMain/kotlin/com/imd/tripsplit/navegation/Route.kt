package com.imd.tripsplit.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route

    @Serializable
    @SerialName("trip")
    data class TripDetail(val tripId: String) : Route

    @Serializable
    data object NewTrip : Route
}

const val TRIP_DEEP_LINK_BASE = "tripsplit://trip"