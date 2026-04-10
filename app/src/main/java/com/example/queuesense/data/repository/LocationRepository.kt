package com.example.queuesense.data.repository

import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import com.example.queuesense.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocations(): Flow<List<QueueLocation>>
    fun getReviews(locationId: String): Flow<List<Review>>
    suspend fun addReview(review: Review)
    suspend fun updateLocationStatus(locationId: String, newStatus: String, waitTime: String, peopleCount: Int, userId: String)
    
    // User Profile
    suspend fun saveUserProfile(profile: UserProfile)
    fun getUserProfile(uid: String): Flow<UserProfile?>
    
    // History
    fun getUserHistory(uid: String): Flow<List<QueueHistory>>
}

data class QueueHistory(
    val id: String = "",
    val locationId: String = "",
    val locationName: String = "",
    val status: String = "",
    val timestamp: java.util.Date = java.util.Date()
)
