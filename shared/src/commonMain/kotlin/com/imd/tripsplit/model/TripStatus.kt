package com.imd.tripsplit.model

import kotlinx.serialization.Serializable

@Serializable
enum class TripStatus {
    ONGOING,
    UPCOMING,
    FINISHED
}
