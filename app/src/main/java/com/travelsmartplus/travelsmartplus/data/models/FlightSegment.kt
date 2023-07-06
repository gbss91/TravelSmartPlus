package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration


data class FlightSegment(
    @JsonProperty("id") val id: Int? =0,
    @JsonProperty("flights") val flights: List<Flight>,
    @JsonProperty("direction") val direction: String,
    @JsonProperty("duration") val duration: Duration,
    @JsonProperty("stops") val stops: Int
)
