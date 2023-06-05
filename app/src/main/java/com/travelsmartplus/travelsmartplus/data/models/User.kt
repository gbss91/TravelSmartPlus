package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("orgId") val orgId: Int,
    @JsonProperty("firstName") var firstName: String,
    @JsonProperty("lastName") var lastName: String,
    @JsonProperty("email") var email: String,
    @JsonProperty("admin") var admin: Boolean,
    @JsonProperty("password") var password: String,
    @JsonProperty("salt") var salt: String,
    @JsonProperty("accountSetup") var accountSetup: Boolean,
    @JsonProperty("preferredAirlines") var preferredAirlines: Set<String>? = null,
    @JsonProperty("preferredHotelChains") var preferredHotelChains: Set<String>? = null
)
