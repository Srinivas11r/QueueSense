package com.example.queuesense.data.repository

import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.model.Review
import com.example.queuesense.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

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

    override suspend fun updateLocationStatus(
        locationId: String, 
        newStatus: String, 
        waitTime: String, 
        peopleCount: Int,
        userId: String
    ) {
        // Update location status
        val locationDoc = firestore.collection("locations").document(locationId)
        val locationSnap = locationDoc.get().await()
        val locationName = locationSnap.getString("name") ?: "Unknown Location"

        locationDoc.update(
            mapOf(
                "status" to newStatus,
                "waitTime" to waitTime,
                "peopleCount" to peopleCount
            )
        ).await()

        // Add to history
        val historyEntry = QueueHistory(
            id = firestore.collection("history").document().id,
            locationId = locationId,
            locationName = locationName,
            status = newStatus,
            timestamp = Date()
        )
        firestore.collection("users").document(userId)
            .collection("history").document(historyEntry.id).set(historyEntry).await()
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        firestore.collection("users").document(profile.uid).set(profile).await()
    }

    override fun getUserProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        val subscription = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val profile = snapshot.toObject(UserProfile::class.java)
                    trySend(profile)
                } else {
                    trySend(null)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getUserHistory(uid: String): Flow<List<QueueHistory>> = callbackFlow {
        val subscription = firestore.collection("users").document(uid)
            .collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val history = snapshot.toObjects(QueueHistory::class.java)
                    trySend(history)
                }
            }
        awaitClose { subscription.remove() }
    }
}
