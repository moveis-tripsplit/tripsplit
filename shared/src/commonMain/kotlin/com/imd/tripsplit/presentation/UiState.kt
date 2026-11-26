package com.imd.tripsplit.presentation

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Error(val message: String) : UiState<Nothing>

    data object Empty : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>
}
