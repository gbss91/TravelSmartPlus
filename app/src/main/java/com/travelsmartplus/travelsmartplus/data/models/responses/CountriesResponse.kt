package com.travelsmartplus.travelsmartplus.data.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class CountriesResponse(
    @JsonProperty("error") val error: Boolean,
    @JsonProperty("msg") val msg: String,
    @JsonProperty("data") val data: List<CountryData>
)

data class CountryData(
    @JsonProperty("iso2") val iso2: String,
    @JsonProperty("iso3") val iso3: String,
    @JsonProperty("country") val country: String,
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("cities") val cities: List<String>? = null
)
