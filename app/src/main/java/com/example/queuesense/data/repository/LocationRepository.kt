package com.example.queuesense.data.repository

import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocations(): Flow<List<QueueLocation>>
    fun getReviews(locationId: String): Flow<List<Review>>
    suspend fun addReview(review: Review)
    suspend fun updateLocationStatus(locationId: String, newStatus: String)
}
