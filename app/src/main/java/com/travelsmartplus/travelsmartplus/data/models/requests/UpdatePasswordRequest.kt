package com.travelsmartplus.travelsmartplus.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordRequest(
    val userId: Int,
    val newPassword: String
)