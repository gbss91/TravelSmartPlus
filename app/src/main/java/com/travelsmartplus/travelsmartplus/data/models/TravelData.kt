package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class TravelData(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("dob") val dob: LocalDate,
    @JsonProperty("nationality") val nationality: String,
    @JsonProperty("passportNumber") val passportNumber: String,
    @JsonProperty("passportExpiryDate") val passportExpiryDate: LocalDate
)
