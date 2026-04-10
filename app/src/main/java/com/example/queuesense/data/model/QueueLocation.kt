package com.example.queuesense.data.model

import java.util.Calendar

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
    val distance: Double? = null,
    
    // Professional features: Timings and Status
    val openTime: String = "09:00",
    val closeTime: String = "17:00",
    val breakStart: String = "13:00",
    val breakEnd: String = "14:00",
    val workingDays: List<Int> = listOf(2, 3, 4, 5, 6) // Mon-Fri (Calendar.MONDAY = 2)
) {
    fun getOperationalStatus(): OperationalStatus {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
        
        if (!workingDays.contains(dayOfWeek)) return OperationalStatus.CLOSED_TODAY
        
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)
        val currentTimeInMinutes = currentHour * 60 + currentMinute

        val (openH, openM) = openTime.split(":").map { it.toInt() }
        val (closeH, closeM) = closeTime.split(":").map { it.toInt() }
        val (breakSH, breakSM) = breakStart.split(":").map { it.toInt() }
        val (breakEH, breakEM) = breakEnd.split(":").map { it.toInt() }

        val openTimeM = openH * 60 + openM
        val closeTimeM = closeH * 60 + closeM
        val breakStartM = breakSH * 60 + breakSM
        val breakEndM = breakEH * 60 + breakEM

        return when {
            currentTimeInMinutes < openTimeM -> OperationalStatus.OPENING_SOON
            currentTimeInMinutes >= closeTimeM -> OperationalStatus.CLOSED
            currentTimeInMinutes in breakStartM..breakEndM -> OperationalStatus.ON_BREAK
            else -> OperationalStatus.OPEN
        }
    }
}

enum class OperationalStatus(val label: String, val colorHex: Long) {
    OPEN("Open Now", 0xFF4CAF50),
    CLOSED("Closed", 0xFFF44336),
    ON_BREAK("Lunch Break", 0xFFFF9800),
    OPENING_SOON("Opening Soon", 0xFF2196F3),
    CLOSED_TODAY("Closed Today", 0xFF9E9E9E)
}
