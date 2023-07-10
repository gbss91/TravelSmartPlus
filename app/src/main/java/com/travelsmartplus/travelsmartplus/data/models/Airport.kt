package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Airport(
    val id: Int,
    val airportName: String,
    val city: String,
    val country: String,
    val iataCode: String,
    val icaoCode: String,
    val latitude: Double,
    val longitude: Double
)




