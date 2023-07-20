package com.travelsmartplus.travelsmartplus.data.models

import com.travelsmartplus.travelsmartplus.utils.BigDecimalSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.math.BigDecimal


@Serializable
data class Booking(
    val id: Int? = 0,
    val user: User,
    val origin: Airport,
    val destination: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate?,
    var flightBooking: FlightBooking,
    var hotelBooking: HotelBooking?,
    val adultsNumber: Int,
    var status: String,
    @Serializable(with = BigDecimalSerializer::class)
    var totalPrice: BigDecimal,
    var imageUrl: String? = null
)
