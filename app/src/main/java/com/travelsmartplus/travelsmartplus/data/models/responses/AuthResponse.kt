package com.travelsmartplus.travelsmartplus.data.models.responses

data class AuthResponse(
    val token: String,
    val refreshToken: String
)
