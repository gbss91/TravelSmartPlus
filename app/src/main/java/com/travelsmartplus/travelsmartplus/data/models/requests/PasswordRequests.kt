package com.travelsmartplus.travelsmartplus.data.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdatePasswordRequest(
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("newPassword") val newPassword: String
)