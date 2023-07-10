package com.travelsmartplus.travelsmartplus.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class CountriesResponse(
    val error: Boolean,
    val msg: String,
    val data: List<CountryData>
)

@Serializable
data class CountryData(
    val iso2: String,
    val iso3: String,
    val country: String,
    val cities: List<String>? = null
)
