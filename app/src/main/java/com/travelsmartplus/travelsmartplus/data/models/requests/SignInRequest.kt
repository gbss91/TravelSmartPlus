package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class SignInRequest(
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String
)
