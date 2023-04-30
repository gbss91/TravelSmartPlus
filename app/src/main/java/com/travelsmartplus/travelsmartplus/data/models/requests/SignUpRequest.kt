package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpRequest(
    @JsonProperty("firstName") val firstName: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("orgName") val orgName: String,
    @JsonProperty("duns") val duns: Int
)