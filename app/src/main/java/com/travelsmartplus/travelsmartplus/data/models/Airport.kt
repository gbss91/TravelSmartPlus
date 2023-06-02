package com.travelsmartplus.travelsmartplus.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Airport(
    @JsonProperty("id") val id: Int,
    @JsonProperty("airportName")val airportName: String,
    @JsonProperty("city") val city: String,
    @JsonProperty("country") val country: String,
    @JsonProperty("iataCode") val iataCode: String,
    @JsonProperty("icaoCode") val icaoCode: String,
    @JsonProperty("latitude") val latitude: Double,
    @JsonProperty("longitude") val longitude: Double
)




