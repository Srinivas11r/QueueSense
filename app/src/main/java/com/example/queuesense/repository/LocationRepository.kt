package com.example.queuesense.repository

import com.example.queuesense.data.model.QueueLocation
import com.example.queuesense.data.source.sampleLocations

class LocationRepository {

    fun getLocationsByCity(city: String): List<QueueLocation> {
        return sampleLocations.filter { it.city == city }
    }

    fun searchLocations(city: String, query: String): List<QueueLocation> {
        return sampleLocations.filter {
            it.city == city &&
                    (
                            it.name.contains(query, true) ||
                                    it.category.contains(query, true)

                            )
        }
    }

    fun getLocationByName(name: String): QueueLocation? {
        return sampleLocations.find { it.name == name }
    }
}
