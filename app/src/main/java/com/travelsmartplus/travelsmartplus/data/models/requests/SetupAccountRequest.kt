package com.travelsmartplus.travelsmartplus.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SetupAccountRequest(
    var newPassword: String,
    var preferredAirlines: List<String>,
    var preferredHotelChains: List<String>
)
