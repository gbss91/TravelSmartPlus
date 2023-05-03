package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    @JsonProperty("id") val id: Int? = 0,
    @JsonProperty("orgId") val orgId: Int,
    @JsonProperty("firstName") val firstName: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("salt") val salt: String
)
