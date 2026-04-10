package com.example.queuesense.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import com.example.queuesense.data.model.UserProfile
import com.example.queuesense.data.repository.LocationRepository
import com.example.queuesense.data.repository.LocationRepositoryImpl
import com.example.queuesense.data.repository.QueueHistory
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.example.queuesense.data.source.sampleLocations
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

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

    private val _userHistory = MutableStateFlow<List<QueueHistory>>(emptyList())
    val userHistory: StateFlow<List<QueueHistory>> = _userHistory.asStateFlow()

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
            val user = firebaseAuth.currentUser
            _currentUser.value = user
            if (user != null) {
                fetchUserProfile(user.uid)
                fetchUserHistory(user.uid)
            } else {
                _userProfile.value = null
                _userHistory.value = emptyList()
            }
        }
    }

    private fun fetchUserProfile(uid: String) {
        viewModelScope.launch {
            repository.getUserProfile(uid).collectLatest { profile ->
                if (profile == null && _currentUser.value != null) {
                    val user = _currentUser.value!!
                    val newProfile = UserProfile(
                        uid = user.uid,
                        displayName = user.displayName ?: "",
                        email = user.email ?: ""
                    )
                    repository.saveUserProfile(newProfile)
                } else {
                    _userProfile.value = profile
                }
            }
        }
    }

    fun updateProfile(name: String, bio: String) {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            val updatedProfile = _userProfile.value?.copy(displayName = name, bio = bio)
                ?: UserProfile(uid = user.uid, displayName = name, bio = bio, email = user.email ?: "")
            
            // Update Firebase Auth display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            
            // Update Firestore
            repository.saveUserProfile(updatedProfile)
        }
    }

    private fun fetchUserHistory(uid: String) {
        viewModelScope.launch {
            repository.getUserHistory(uid).collectLatest { history ->
                _userHistory.value = history
            }
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

    fun submitReport(locationId: String, status: String, waitTime: String, peopleCount: Int) {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                repository.updateLocationStatus(locationId, status, waitTime, peopleCount, user.uid)
            } catch (e: Exception) {
            }
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun seedDatabase() {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            for (loc in sampleLocations) {
                db.collection("locations").document(loc.id).set(loc).await()
            }
        }
    }

    private fun getSampleData(): List<QueueLocation> {
        return sampleLocations
    }
}
