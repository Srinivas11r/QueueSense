package com.example.queuesense.data.repository

import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationRepositoryImpl(
    private val firestore: FirebaseFirestore
) : LocationRepository {

    override fun getLocations(): Flow<List<QueueLocation>> = callbackFlow {
        val subscription = firestore.collection("locations")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val locations = snapshot.toObjects(QueueLocation::class.java)
                    trySend(locations)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getReviews(locationId: String): Flow<List<Review>> = callbackFlow {
        val subscription = firestore.collection("reviews")
            .whereEqualTo("locationId", locationId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val reviews = snapshot.toObjects(Review::class.java)
                    trySend(reviews)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun addReview(review: Review) {
        firestore.collection("reviews").add(review).await()
    }

    override suspend fun updateLocationStatus(locationId: String, newStatus: String) {
        firestore.collection("locations").document(locationId)
            .update("status", newStatus).await()
    }
}
