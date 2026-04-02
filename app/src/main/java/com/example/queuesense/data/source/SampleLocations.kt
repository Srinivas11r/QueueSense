package com.example.queuesense.data.source

import com.example.queuesense.data.model.QueueLocation

val sampleLocations: List<QueueLocation> = listOf(
    QueueLocation(
        id = "1",
        name = "RTO Office",
        city = "Anantapur",
        category = "Govt",
        status = "Long",
        waitTime = "45 mins",
        peopleCount = 50,
        bestTime = "2 PM",
        latitude = 14.6819,
        longitude = 77.6006
    ),
    QueueLocation(
        id = "2",
        name = "Apollo Hospital",
        city = "Bangalore",
        category = "Health",
        status = "Moderate",
        waitTime = "20 mins",
        peopleCount = 15,
        bestTime = "10 AM",
        latitude = 12.9716,
        longitude = 77.5946
    ),
    QueueLocation(
        id = "3",
        name = "Axis Bank",
        city = "Anantapur",
        category = "Banks",
        status = "Short",
        waitTime = "10 mins",
        peopleCount = 5,
        bestTime = "11 AM",
        latitude = 14.6819,
        longitude = 77.6006
    )
)
