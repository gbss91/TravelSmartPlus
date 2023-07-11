package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TravelData(
    val id: Int? = 0,
    val userId: Int,
    val dob: LocalDate,
    val nationality: String,
    val passportNumber: String,
    val passportExpiryDate: LocalDate
)
