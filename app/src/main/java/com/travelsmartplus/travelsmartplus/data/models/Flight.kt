package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Flight(
    val id: Int? = 0,
    val departureAirport: Airport,
    val departureTime: LocalDateTime,
    val arrivalAirport: Airport,
    val arrivalTime: LocalDateTime,
    val carrierIataCode: String,
    val carrierName: String
)
