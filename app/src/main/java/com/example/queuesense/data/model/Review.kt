package com.example.queuesense.data.model

import java.util.Date

data class Review(
    val id: String = "",
    val locationId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhoto: String? = null,
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Date? = null
)
