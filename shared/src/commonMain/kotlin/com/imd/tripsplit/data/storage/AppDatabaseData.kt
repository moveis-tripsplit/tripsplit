package com.imd.tripsplit.data.storage

import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.User
import kotlinx.serialization.Serializable

@Serializable
data class AppDatabaseData(
    val users: List<User> = emptyList(),
    val trips: List<Trip> = emptyList(),
    val currentUserId: String? = null,
)
