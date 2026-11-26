package com.imd.tripsplit.presentation.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imd.tripsplit.domain.model.Trip
import com.imd.tripsplit.domain.usecase.CreateTripUseCase
import com.imd.tripsplit.domain.usecase.GetTripUseCase
import com.imd.tripsplit.domain.usecase.UpdateTripUseCase
import com.imd.tripsplit.presentation.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateTripViewModel(
    private val tripId: String?,
    private val getTrip: GetTripUseCase,
    private val createTrip: CreateTripUseCase,
    private val updateTrip: UpdateTripUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateTripUiState())
    val state: StateFlow<CreateTripUiState> = _state.asStateFlow()

    private var editingTrip: Trip? = null

    init {
        if (tripId != null) {
            viewModelScope.launch {
                try {
                    editingTrip = getTrip(tripId)
                    _state.value = editingTrip?.toCreateTripUiState() ?: CreateTripUiState()
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (error: Exception) {
                    _state.value = _state.value.copy(
                        submissionState = UiState.Error(error.message ?: "Não foi possível carregar a viagem."),
                    )
                }
            }
        }
    }

    fun onAction(action: CreateTripAction) {
        _state.value = _state.value.reduce(action)
    }

    fun submit(onSuccess: (String) -> Unit) {
        val form = _state.value
        viewModelScope.launch {
            _state.value = form.copy(submissionState = UiState.Loading)
            val savedTripId =
                try {
                    val draft = form.toTripDraft()
                    val trip = editingTrip?.let { updateTrip(it, draft) } ?: createTrip(draft)
                    editingTrip = trip
                    _state.value = _state.value.copy(submissionState = UiState.Success(Unit))
                    trip.id
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (error: Exception) {
                    _state.value = _state.value.copy(
                        submissionState = UiState.Error(error.message ?: "Não foi possível salvar a viagem."),
                    )
                    return@launch
                }
            onSuccess(savedTripId)
        }
    }
}
