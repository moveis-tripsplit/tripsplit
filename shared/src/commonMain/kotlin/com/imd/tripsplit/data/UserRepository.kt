package com.imd.tripsplit.data

import com.imd.tripsplit.model.User
import kotlinx.coroutines.flow.StateFlow

object UserRepository {
    val currentUser: StateFlow<User?> = LocalDataManager.currentUser

    fun register(
        name: String,
        email: String,
        password: String,
        birthDate: String?,
    ): Result<User> {
        val user =
            User(
                id = "user-${kotlin.random.Random.nextInt(1000, 99999)}",
                name = name.trim(),
                email = email.trim(),
                password = password,
                birthDate = birthDate,
            )
        return LocalDataManager.registerUser(user)
    }

    fun login(email: String, password: String): Result<User> {
        return LocalDataManager.login(email = email, password = password)
    }

    fun logout() {
        LocalDataManager.logout()
    }

    fun isLoggedIn(): Boolean = currentUser.value != null
}
