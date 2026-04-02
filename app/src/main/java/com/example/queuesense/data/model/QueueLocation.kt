package com.example.queuesense.data.model

data class QueueLocation(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val category: String = "",
    val status: String = "Short", // Short, Moderate, Long
    val waitTime: String = "0 mins",
    val peopleCount: Int = 0,
    val bestTime: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: Double? = null
)
