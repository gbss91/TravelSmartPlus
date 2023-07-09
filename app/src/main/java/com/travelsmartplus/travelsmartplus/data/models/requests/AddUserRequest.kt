package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddUserRequest(
    @JsonProperty("firstName") var firstName: String,
    @JsonProperty("lastName") var lastName: String,
    @JsonProperty("email") var email: String,
    @JsonProperty("admin") var admin: Boolean,
    @JsonProperty("password") var password: String
)
