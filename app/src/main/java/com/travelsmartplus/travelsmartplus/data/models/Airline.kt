package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Airline(
    @JsonProperty("id") val id: Int,
    @JsonProperty("airlineName") val airlineName: String,
    @JsonProperty("iataCode") val iataCode: String,
    @JsonProperty("icaoCode") val icaoCode: String
)
