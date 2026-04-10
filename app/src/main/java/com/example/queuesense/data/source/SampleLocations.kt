package com.example.queuesense.data.source

import com.example.queuesense.data.model.QueueLocation

val sampleLocations: List<QueueLocation> = listOf(
    // KADIRI LOCATIONS
    QueueLocation(
        id = "K01", name = "Kadiri RTO Office", city = "Kadiri", category = "Govt",
        status = "Moderate", waitTime = "30 mins", peopleCount = 25, bestTime = "10 AM",
        latitude = 14.1147, longitude = 78.1598,
        openTime = "10:00", closeTime = "17:00", breakStart = "13:00", breakEnd = "14:00"
    ),
    QueueLocation(
        id = "K02", name = "Kadiri Govt Hospital", city = "Kadiri", category = "Health",
        status = "Long", waitTime = "1 hour", peopleCount = 60, bestTime = "9 AM",
        latitude = 14.1200, longitude = 78.1650,
        openTime = "00:00", closeTime = "23:59", breakStart = "13:30", breakEnd = "14:00"
    ),
    QueueLocation(
        id = "K03", name = "SBI Main Branch Kadiri", city = "Kadiri", category = "Banks",
        status = "Short", waitTime = "5 mins", peopleCount = 4, bestTime = "4 PM",
        latitude = 14.1100, longitude = 78.1500,
        openTime = "10:00", closeTime = "16:00", breakStart = "14:00", breakEnd = "14:30"
    ),
    QueueLocation(
        id = "K04", name = "Kadiri Head Post Office", city = "Kadiri", category = "Govt",
        status = "Moderate", waitTime = "15 mins", peopleCount = 12, bestTime = "11 AM",
        latitude = 14.1150, longitude = 78.1600,
        openTime = "09:00", closeTime = "17:00", breakStart = "13:00", breakEnd = "13:30"
    ),
    QueueLocation(
        id = "K05", name = "Sri Kadiri Lakshmi Narasimha Swamy Temple", city = "Kadiri", category = "Religious",
        status = "Long", waitTime = "45 mins", peopleCount = 150, bestTime = "4 AM",
        latitude = 14.1130, longitude = 78.1610,
        openTime = "04:30", closeTime = "21:00", breakStart = "13:00", breakEnd = "15:30"
    ),
    QueueLocation(
        id = "K06", name = "Hotel Grand Ridge", city = "Kadiri", category = "Hotels",
        status = "Short", waitTime = "10 mins", peopleCount = 8, bestTime = "8 PM",
        latitude = 14.1180, longitude = 78.1550,
        openTime = "07:00", closeTime = "23:00", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "K07", name = "Srinivasa Theatre", city = "Kadiri", category = "Cinemas",
        status = "Moderate", waitTime = "20 mins", peopleCount = 40, bestTime = "6 PM",
        latitude = 14.1220, longitude = 78.1680,
        openTime = "10:00", closeTime = "01:00", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "K08", name = "Andhra Bank (UBI) Kadiri", city = "Kadiri", category = "Banks",
        status = "Moderate", waitTime = "25 mins", peopleCount = 18, bestTime = "10:30 AM",
        latitude = 14.1120, longitude = 78.1520,
        openTime = "10:00", closeTime = "16:00", breakStart = "14:00", breakEnd = "14:30"
    ),
    
    // ANANTAPUR LOCATIONS
    QueueLocation(
        id = "A01", name = "Anantapur Railway Station", city = "Anantapur", category = "Transport",
        status = "Long", waitTime = "40 mins", peopleCount = 80, bestTime = "11 PM",
        latitude = 14.6819, longitude = 77.6006,
        openTime = "00:00", closeTime = "23:59", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "A02", name = "D-Mart Anantapur", city = "Anantapur", category = "Stores",
        status = "Moderate", waitTime = "20 mins", peopleCount = 45, bestTime = "11 AM",
        latitude = 14.6700, longitude = 77.6100,
        openTime = "09:00", closeTime = "22:00", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "A03", name = "KIMS Saveera Hospital", city = "Anantapur", category = "Health",
        status = "Moderate", waitTime = "25 mins", peopleCount = 35, bestTime = "2 PM",
        latitude = 14.6600, longitude = 77.5900,
        openTime = "00:00", closeTime = "23:59", breakStart = "13:00", breakEnd = "14:00"
    ),
    QueueLocation(
        id = "A04", name = "HDFC Bank Tower", city = "Anantapur", category = "Banks",
        status = "Short", waitTime = "10 mins", peopleCount = 6, bestTime = "10 AM",
        latitude = 14.6850, longitude = 77.6100,
        openTime = "09:30", closeTime = "15:30", breakStart = "13:00", breakEnd = "13:30"
    ),
    QueueLocation(
        id = "A05", name = "PVR Cinemas Anantapur", city = "Anantapur", category = "Cinemas",
        status = "Short", waitTime = "5 mins", peopleCount = 12, bestTime = "2 PM",
        latitude = 14.6900, longitude = 77.6200,
        openTime = "10:00", closeTime = "00:00", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "A06", name = "Collectorate Office", city = "Anantapur", category = "Govt",
        status = "Long", waitTime = "50 mins", peopleCount = 55, bestTime = "10 AM",
        latitude = 14.6830, longitude = 77.6020,
        openTime = "10:00", closeTime = "17:00", breakStart = "13:00", breakEnd = "14:00"
    ),
    QueueLocation(
        id = "A07", name = "Post Office Head", city = "Anantapur", category = "Govt",
        status = "Moderate", waitTime = "15 mins", peopleCount = 20, bestTime = "10:30 AM",
        latitude = 14.6840, longitude = 77.6030,
        openTime = "09:00", closeTime = "17:00", breakStart = "13:00", breakEnd = "13:30"
    ),

    // BANGALORE LOCATIONS
    QueueLocation(
        id = "B01", name = "Manyata Tech Park", city = "Bangalore", category = "Office",
        status = "Moderate", waitTime = "10 mins", peopleCount = 1200, bestTime = "11 AM",
        latitude = 13.0451, longitude = 77.6266,
        openTime = "00:00", closeTime = "23:59", breakStart = "13:00", breakEnd = "14:00"
    ),
    QueueLocation(
        id = "B02", name = "PVR Orion Mall", city = "Bangalore", category = "Cinemas",
        status = "Long", waitTime = "30 mins", peopleCount = 150, bestTime = "6 PM",
        latitude = 12.9895, longitude = 77.5545,
        openTime = "10:00", closeTime = "01:00", breakStart = "00:00", breakEnd = "00:00"
    ),
    QueueLocation(
        id = "B03", name = "HDFC Bank MG Road", city = "Bangalore", category = "Banks",
        status = "Short", waitTime = "5 mins", peopleCount = 10, bestTime = "10 AM",
        latitude = 12.9740, longitude = 77.6080,
        openTime = "09:30", closeTime = "15:30", breakStart = "13:00", breakEnd = "13:30"
    ),
    QueueLocation(
        id = "B04", name = "Indiranagar Post Office", city = "Bangalore", category = "Govt",
        status = "Moderate", waitTime = "20 mins", peopleCount = 25, bestTime = "11 AM",
        latitude = 12.9719, longitude = 77.6412,
        openTime = "09:00", closeTime = "17:00", breakStart = "13:00", breakEnd = "13:30"
    ),
    QueueLocation(
        id = "B05", name = "The Leela Palace", city = "Bangalore", category = "Hotels",
        status = "Short", waitTime = "0 mins", peopleCount = 45, bestTime = "8 PM",
        latitude = 12.9606, longitude = 77.6484,
        openTime = "00:00", closeTime = "23:59", breakStart = "00:00", breakEnd = "00:00"
    )
)
