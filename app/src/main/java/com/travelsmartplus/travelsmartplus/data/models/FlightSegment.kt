package com.travelsmartplus.travelsmartplus.data.models


import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class FlightSegment(
    val id: Int? =0,
    val flights: List<Flight>,
    val direction: String,
    val duration: Duration,
    val stops: Int
)
