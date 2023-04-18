package com.travelsmartplus.travelsmartplus.models.requests

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val orgName: String,
    val duns: Int
)