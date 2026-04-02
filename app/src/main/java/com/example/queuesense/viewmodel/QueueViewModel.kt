package com.example.queuesense.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import com.example.queuesense.data.model.UserProfile
import com.example.queuesense.data.repository.LocationRepository
import com.example.queuesense.data.repository.LocationRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel for managing queue-related data and user state.
 * Following MVVM architecture pattern.
 */
class QueueViewModel(
    private val repository: LocationRepository = LocationRepositoryImpl(FirebaseFirestore.getInstance()),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _locations = MutableStateFlow<List<QueueLocation>>(emptyList())
    val locations: StateFlow<List<QueueLocation>> = _locations.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        observeAuthState()
        loadLocations()
    }

    private fun observeAuthState() {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getLocations().collect { list ->
                val data = if (list.isEmpty()) getSampleData() else list
                _locations.value = calculateDistances(data, _userLocation.value)
                _isLoading.value = false
            }
        }
    }

    fun setUserLocation(latLng: LatLng) {
        _userLocation.value = latLng
        _locations.value = calculateDistances(_locations.value, latLng)
    }

    private fun calculateDistances(locations: List<QueueLocation>, userLoc: LatLng?): List<QueueLocation> {
        if (userLoc == null) return locations
        
        return locations.map { loc ->
            val results = FloatArray(1)
            Location.distanceBetween(
                userLoc.latitude, userLoc.longitude,
                loc.latitude, loc.longitude,
                results
            )
            val distanceInKm = results[0] / 1000.0
            loc.copy(distance = Math.round(distanceInKm * 10.0) / 10.0)
        }.sortedBy { it.distance }
    }

    fun fetchReviews(locationId: String) {
        viewModelScope.launch {
            repository.getReviews(locationId).collect {
                _reviews.value = it
            }
        }
    }

    fun addReview(locationId: String, rating: Int, comment: String) {
        val user = auth.currentUser ?: return
        val review = Review(
            locationId = locationId,
            userId = user.uid,
            userName = user.displayName ?: "Anonymous User",
            userPhoto = user.photoUrl?.toString() ?: "",
            rating = rating,
            comment = comment,
            createdAt = Date()
        )
        viewModelScope.launch {
            try {
                repository.addReview(review)
            } catch (e: Exception) {
            }
        }
    }

    fun logout() {
        auth.signOut()
    }

    private fun getSampleData(): List<QueueLocation> {
        return listOf(
            QueueLocation("1", "RTO Office", "Anantapur", "Govt", "Long", "45 mins", 50, "2 PM", 14.6819, 77.6006),
            QueueLocation("2", "Apollo Hospital", "Bangalore", "Health", "Moderate", "20 mins", 15, "10 AM", 12.9716, 77.5946),
            QueueLocation("3", "Axis Bank", "Anantapur", "Banks", "Short", "10 mins", 5, "11 AM", 14.6548, 77.5562)
        )
    }
}
