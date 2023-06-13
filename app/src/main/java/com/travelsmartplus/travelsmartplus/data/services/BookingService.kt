package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.AIRPORTS_SEARCH
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.BOOKING_SEARCH
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookingService {

    @GET(AIRPORTS_SEARCH)
    suspend fun getAllAirports(): Response<List<Airport>>

    @POST(BOOKING_SEARCH)
    suspend fun bookingSearch(@Body bookingSearchRequest: BookingSearchRequest): Response<Booking>
}