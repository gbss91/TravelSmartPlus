package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.Airline
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.data.models.Hotel
import com.travelsmartplus.travelsmartplus.data.models.HotelBooking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.ADD_BOOKING
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.AIRLINES_SEARCH
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.AIRPORTS_SEARCH
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.BOOKING_SEARCH
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.DELETE_BOOKING
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_ALL_BOOKINGS
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_BOOKING
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_FLIGHT_OFFERS
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_HOTEL_OFFERS
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_USER_BOOKINGS
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.HOTELS_SEARCH
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingService {

    @GET(AIRPORTS_SEARCH)
    suspend fun getAllAirports(): Response<List<Airport>>

    @GET(AIRLINES_SEARCH)
    suspend fun getAllAirlines(): Response<List<Airline>>

    @GET(HOTELS_SEARCH)
    suspend fun getAllHotels(): Response<List<Hotel>>

    @POST(BOOKING_SEARCH)
    suspend fun bookingSearch(@Body bookingSearchRequest: BookingSearchRequest): Response<Booking>

    @POST(GET_FLIGHT_OFFERS)
    suspend fun getFlightOffers(@Body bookingSearchRequest: BookingSearchRequest): Response<List<FlightBooking>>

    @POST(GET_HOTEL_OFFERS)
    suspend fun getHotelOffers(@Body bookingSearchRequest: BookingSearchRequest): Response<List<HotelBooking>>

    @GET(GET_USER_BOOKINGS)
    suspend fun getUserBookings(@Path("userId") userId: String): Response<List<Booking>>

    @POST(ADD_BOOKING)
    suspend fun newBooking(@Body booking: Booking): Response<Booking>

    @GET(GET_BOOKING)
    suspend fun getBooking(@Path("id") id: String): Response<Booking>

    @DELETE(DELETE_BOOKING)
    suspend fun deleteBooking(@Path("id") id: String): Response<Unit>

    @GET(GET_ALL_BOOKINGS)
    suspend fun getAllBookings(@Path("orgId") id: String): Response<List<Booking>>

}