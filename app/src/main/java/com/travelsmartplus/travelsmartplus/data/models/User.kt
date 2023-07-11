package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = 0,
    val orgId: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var admin: Boolean,
    var password: String,
    var salt: String,
    var accountSetup: Boolean,
    var preferredAirlines: List<String>? = null,
    var preferredHotelChains: List<String>? = null,
    var travelData: TravelData? = null
)