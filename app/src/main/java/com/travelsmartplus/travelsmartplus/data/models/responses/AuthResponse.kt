package com.travelsmartplus.travelsmartplus.data.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponse(
    @JsonProperty("token") val token: String,
    @JsonProperty("refreshToken") val refreshToken: String,
    @JsonProperty("accountSetup") val accountSetup: Boolean
)
