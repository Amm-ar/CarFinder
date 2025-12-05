package com.ammar.carfinder.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.carfinder.data.model.Car
import com.ammar.carfinder.data.model.CarStatus
import com.ammar.carfinder.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AddCarResult {
    object Idle : AddCarResult
    object Loading : AddCarResult
    object Success : AddCarResult
    data class Error(val message: String) : AddCarResult
}

data class CarUiState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val addCarResult: AddCarResult = AddCarResult.Idle
)

class CarViewModel(
    private val repository: CarRepository = CarRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarUiState())
    val uiState: StateFlow<CarUiState> = _uiState.asStateFlow()

    init {
        loadAllCars()
    }

    fun loadAllCars() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val cars = repository.getAllCars()
                _uiState.update { it.copy(cars = cars, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load cars. Please try again.") }
            }
        }
    }

    fun filterCars(status: CarStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val cars = if (status == CarStatus.LOST) repository.getLostCars() else repository.getFoundCars()
                _uiState.update { it.copy(cars = cars, isLoading = false) }
            } catch (e: Exception) {
                 _uiState.update { it.copy(isLoading = false, error = "Failed to filter cars.") }
            }
        }
    }

    fun searchCars(query: String) {
        if (query.isBlank()) {
            loadAllCars()
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val cars = repository.searchCars(query)
                _uiState.update { it.copy(cars = cars, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Search failed. Please try again.") }
            }
        }
    }

    fun addCar(car: Car, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(addCarResult = AddCarResult.Loading) }
            try {
                var finalCar = car
                if (imageUri != null) {
                    val imageUrl = repository.uploadCarImage(context, imageUri)
                    finalCar = car.copy(imageUrl = imageUrl)
                }
                
                repository.addCar(finalCar)
                _uiState.update { it.copy(addCarResult = AddCarResult.Success) }
                loadAllCars() // Refresh list
            } catch (e: Exception) {
                // Showing full error message for debugging
                val errorMessage = e.message ?: "Unknown error occurred"
                _uiState.update { it.copy(addCarResult = AddCarResult.Error(errorMessage)) }
            }
        }
    }
    
    fun resetAddCarStatus() {
        _uiState.update { it.copy(addCarResult = AddCarResult.Idle) }
    }
}
