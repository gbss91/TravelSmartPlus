package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.travelsmartplus.travelsmartplus.data.models.Airport
import kotlinx.datetime.LocalDate


data class BookingSearchRequest(
    @JsonProperty("userId") var userId: Int? = 0,
    @JsonProperty("oneWay") val oneWay: Boolean,
    @JsonProperty("nonStop") val nonStop: Boolean,
    @JsonProperty("origin") val origin: Airport,
    @JsonProperty("destination") val destination: Airport,
    @JsonProperty("departureDate") val departureDate: LocalDate,
    @JsonProperty("returnDate") val returnDate: LocalDate?,
    @JsonProperty("adultsNumber") val adultsNumber: Int,
    @JsonProperty("travelClass") val travelClass: String,
    @JsonProperty("hotel") val hotel: Boolean,
    @JsonProperty("checkInDate") val checkInDate: LocalDate?,
    @JsonProperty("checkOutDate") val checkOutDate: LocalDate?
)
