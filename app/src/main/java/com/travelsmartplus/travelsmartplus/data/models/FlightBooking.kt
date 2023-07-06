package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class FlightBooking(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("bookingReference") val bookingReference: String,
    @JsonProperty("oneWay") val oneWay: Boolean,
    @JsonProperty("originCity") val originCity: String,
    @JsonProperty("destinationCity") val destinationCity: String,
    @JsonProperty("segments") val segments: List<FlightSegment>,
    @JsonProperty("travelClass") val travelClass: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("totalPrice") val totalPrice: BigDecimal,
)
