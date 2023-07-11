package com.travelsmartplus.travelsmartplus.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val accountSetup: Boolean,
    val admin: Boolean,
    val orgId: Int? = null,

    )