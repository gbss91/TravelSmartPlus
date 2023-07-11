package com.travelsmartplus.travelsmartplus.data.models.requests

import com.travelsmartplus.travelsmartplus.data.models.Airport
import kotlinx.serialization.Serializable

@Serializable
data class BookingSearchRequest(
    var userId: Int? = 0,
    val oneWay: Boolean,
    val nonStop: Boolean,
    val origin: Airport,
    val destination: Airport,
    val departureDate: String,
    val returnDate: String?,
    val adultsNumber: Int,
    val travelClass: String,
    val hotel: Boolean,
    val checkInDate: String?,
    val checkOutDate: String?
)