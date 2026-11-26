package com.imd.tripsplit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.model.TripFilter
import com.imd.tripsplit.domain.usecase.ObserveTripsUseCase
import com.imd.tripsplit.presentation.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val observeTrips: ObserveTripsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Trip>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Trip>>> = _state.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TripFilter.ALL)
    val selectedFilter: StateFlow<TripFilter> = _selectedFilter.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                observeTrips().collect { trips ->
                    _state.value = if (trips.isEmpty()) UiState.Empty else UiState.Success(trips)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                _state.value = UiState.Error(error.message ?: "Não foi possível carregar as viagens.")
            }
        }
    }

    fun selectFilter(filter: TripFilter) {
        _selectedFilter.value = filter
    }
}
