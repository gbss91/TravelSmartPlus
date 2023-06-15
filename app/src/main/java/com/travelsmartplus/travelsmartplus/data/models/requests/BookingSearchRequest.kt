package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.travelsmartplus.travelsmartplus.data.models.Airport


data class BookingSearchRequest(
    @JsonProperty("userId") var userId: Int? = 0,
    @JsonProperty("oneWay") val oneWay: Boolean,
    @JsonProperty("nonStop") val nonStop: Boolean,
    @JsonProperty("origin") val origin: Airport,
    @JsonProperty("destination") val destination: Airport,
    @JsonProperty("departureDate") val departureDate: String,
    @JsonProperty("returnDate") val returnDate: String?,
    @JsonProperty("adultsNumber") val adultsNumber: Int,
    @JsonProperty("travelClass") val travelClass: String,
    @JsonProperty("hotel") val hotel: Boolean,
    @JsonProperty("checkInDate") val checkInDate: String?,
    @JsonProperty("checkOutDate") val checkOutDate: String?
)
