package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Flight(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("departureAirport") val departureAirport: Airport,
    @JsonProperty("departureTime") val departureTime: LocalDateTime,
    @JsonProperty("arrivalAirport") val arrivalAirport: Airport,
    @JsonProperty("arrivalTime") val arrivalTime: LocalDateTime,
    @JsonProperty("carrierIataCode") val carrierIataCode: String,
    @JsonProperty("carrierName") val carrierName: String
)
