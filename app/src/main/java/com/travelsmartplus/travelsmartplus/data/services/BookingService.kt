package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.AIRPORTS_SEARCH
import retrofit2.Response
import retrofit2.http.GET

interface BookingService {

    @GET(AIRPORTS_SEARCH)
    suspend fun getAllAirports(): Response<List<Airport>>
}