package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Airline(
    val id: Int,
    val airlineName: String,
    val iataCode: String,
    val icaoCode: String
)
