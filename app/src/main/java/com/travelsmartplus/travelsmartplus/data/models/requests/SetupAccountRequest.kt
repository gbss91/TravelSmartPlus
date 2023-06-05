package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class SetupAccountRequest(
    @JsonProperty("newPassword") var newPassword: String,
    @JsonProperty("preferredAirlines") var preferredAirlines: Set<String>,
    @JsonProperty("preferredHotelChains") var preferredHotelChains: Set<String>
)
