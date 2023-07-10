package com.travelsmartplus.travelsmartplus.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequest(
    var firstName: String,
    var lastName: String,
    var email: String,
    var admin: Boolean,
    var password: String
)
