package com.travelsmartplus.travelsmartplus.data.models

import com.travelsmartplus.utils.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class FlightBooking(
    val id: Int? = 0,
    val bookingReference: String,
    val oneWay: Boolean,
    val originCity: String,
    val destinationCity: String,
    val segments: List<FlightSegment>,
    val travelClass: String,
    var status: String,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
)
