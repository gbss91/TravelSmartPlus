package com.travelsmartplus.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class BookingSearchRequest(
    @JsonProperty("originCity") val originCity: String,
    @JsonProperty("originIataCode") val originIataCode: String,
    @JsonProperty("destinationCity") val destinationCity: String,
    @JsonProperty("destinationIataCode") val destinationIataCode: String,
    @JsonProperty("departureDate") val departureDate: String,
    @JsonProperty("returnDate") val returnDate: String?,
    @JsonProperty("adultsNumber") val adultsNumber: Int,
    @JsonProperty("adultsNumber") val bookingClass: String
)
