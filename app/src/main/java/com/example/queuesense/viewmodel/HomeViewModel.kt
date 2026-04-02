package com.example.queuesense.viewmodel

import android.app.Application
import android.location.Location
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.example.queuesense.LocationHelper
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // 🔹 Existing Repository
    private val repository = LocationRepository()

    // 🔹 GPS Helper
    private val locationHelper = LocationHelper(application)

    // 🔹 UI State (Existing)
    var currentCity by mutableStateOf("Anantapur")
    var searchQuery by mutableStateOf("")

    var locations by mutableStateOf<List<QueueLocation>>(emptyList())
        private set

    // 🔹 NEW: User GPS Location
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    // 🔹 Init
    init {
        loadLocations()
    }

    // 🔹 Load locations by city
    fun loadLocations() {
        locations = repository.getLocationsByCity(currentCity)
    }

    // 🔹 Search logic
    fun onSearch(query: String) {
        searchQuery = query

        locations = if (query.isEmpty()) {
            repository.getLocationsByCity(currentCity)
        } else {
            repository.searchLocations(currentCity, query)
        }
    }

    // 🔹 GET USER GPS LOCATION
    fun getUserLocation() {
        locationHelper.getLocation { location ->
            _userLocation.value = location
        }
    }
}
