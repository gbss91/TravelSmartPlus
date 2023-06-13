package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Hotel(
    @JsonProperty("id") val hotelChain: String,
    @JsonProperty("code") val code: String
)
