package com.travelsmartplus.travelsmartplus.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val hotelChain: String,
    val code: String
)
