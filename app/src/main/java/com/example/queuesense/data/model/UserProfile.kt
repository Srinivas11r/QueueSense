package com.example.queuesense.data.model

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoURL: String? = null,
    val bio: String? = "",
    val role: String = "user"
)
