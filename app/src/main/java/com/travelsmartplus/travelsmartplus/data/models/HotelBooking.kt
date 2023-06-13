package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class HotelBooking(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("hotelName") val hotelName: String,
    @JsonProperty("hotelChainCode") val hotelChainCode: String? = null,
    @JsonProperty("address") val address: String,
    @JsonProperty("checkInDate") val checkInDate: LocalDate,
    @JsonProperty("checkOutDate") val checkOutDate: LocalDate,
    @JsonProperty("rate") val rate: BigDecimal,
    @JsonProperty("totalPrice") val totalPrice: BigDecimal,
    @JsonProperty("latitude") val latitude: Double,
    @JsonProperty("longitude") val longitude: Double
)
