package com.travelsmartplus.travelsmartplus.data.models

import com.travelsmartplus.travelsmartplus.utils.BigDecimalSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class HotelBooking(
    val id: Int? = 0,
    val hotelName: String,
    val hotelChainCode: String? = null,
    val address: String,
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate,
    val roomType: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val rate: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    val latitude: Double,
    val longitude: Double
)
