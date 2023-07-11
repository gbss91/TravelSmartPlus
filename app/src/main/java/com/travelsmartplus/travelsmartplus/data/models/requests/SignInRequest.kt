package com.travelsmartplus.travelsmartplus.data.models.requests

import kotlinx.serialization.Serializable


@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)