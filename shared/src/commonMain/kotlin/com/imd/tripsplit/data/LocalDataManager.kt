package com.imd.tripsplit.data

import com.imd.tripsplit.data.storage.AppDatabaseData
import com.imd.tripsplit.data.storage.PlatformStorage
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

object LocalDataManager {
    private const val DB_FILE_NAME = "tripsplit_db.json"
    private val storage = PlatformStorage()
    private val json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }

    private val _dbState = MutableStateFlow(loadInitialData())

    val users: StateFlow<List<User>>
        get() = MutableStateFlow(_dbState.value.users)

    private val _currentUser = MutableStateFlow<User?>(_dbState.value.users.find { it.id == _dbState.value.currentUserId })
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _trips = MutableStateFlow<List<Trip>>(_dbState.value.trips)
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    private fun loadInitialData(): AppDatabaseData {
        val raw = storage.readString(DB_FILE_NAME) ?: return AppDatabaseData()
        return try {
            json.decodeFromString<AppDatabaseData>(raw)
        } catch (e: Exception) {
            e.printStackTrace()
            AppDatabaseData()
        }
    }

    private fun persist() {
        val current = _dbState.value
        try {
            val encoded = json.encodeToString(AppDatabaseData.serializer(), current)
            storage.writeString(DB_FILE_NAME, encoded)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- USUÁRIOS & AUTENTICAÇÃO ---

    fun registerUser(user: User): Result<User> {
        val current = _dbState.value
        if (current.users.any { it.email.equals(user.email.trim(), ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("Já existe um usuário cadastrado com este e-mail."))
        }

        val updatedUsers = current.users + user
        _dbState.update { it.copy(users = updatedUsers, currentUserId = user.id) }
        _currentUser.value = user
        persist()
        return Result.success(user)
    }

    fun login(
        email: String,
        password: String
    ): Result<User> {
        val found =
            _dbState.value.users.find {
                it.email.equals(email.trim(), ignoreCase = true) && it.password == password
            }

        return if (found != null) {
            _dbState.update { it.copy(currentUserId = found.id) }
            _currentUser.value = found
            persist()
            Result.success(found)
        } else {
            Result.failure(IllegalArgumentException("E-mail ou senha incorretos."))
        }
    }

    fun logout() {
        _dbState.update { it.copy(currentUserId = null) }
        _currentUser.value = null
        persist()
    }

    // --- VIAGENS ---

    fun addTrip(trip: Trip) {
        val loggedId = _currentUser.value?.id ?: ""
        val tripWithUser = if (trip.userId.isBlank()) trip.copy(userId = loggedId) else trip

        _dbState.update { current ->
            current.copy(trips = listOf(tripWithUser) + current.trips)
        }
        _trips.value = _dbState.value.trips
        persist()
    }

    fun getTrip(id: String): Trip? = _trips.value.find { it.id == id }

    fun updateTrip(updated: Trip) {
        _dbState.update { current ->
            current.copy(trips = current.trips.map { if (it.id == updated.id) updated else it })
        }
        _trips.value = _dbState.value.trips
        persist()
    }

    fun getTripsForCurrentUser(): List<Trip> {
        val userId = _currentUser.value?.id
        return if (userId != null) {
            _trips.value.filter { it.userId == userId || it.userId.isBlank() }
        } else {
            _trips.value
        }
    }
}
