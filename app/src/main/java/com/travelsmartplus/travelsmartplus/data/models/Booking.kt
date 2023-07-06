package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class Booking(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("user") val user: User,
    @JsonProperty("origin") val origin: Airport,
    @JsonProperty("destination") val destination: Airport,
    @JsonProperty("departureDate") val departureDate: LocalDate,
    @JsonProperty("returnDate") val returnDate: LocalDate?,
    @JsonProperty("flightBooking") val flightBooking: FlightBooking,
    @JsonProperty("hotelBooking") var hotelBooking: HotelBooking?,
    @JsonProperty("adultsNumber") val adultsNumber: Int,
    @JsonProperty("status") val status: String,
    @JsonProperty("totalPrice") var totalPrice: BigDecimal,
    @JsonProperty("imageUrl") var imageUrl: String? = null
)
