package com.travelsmartplus.travelsmartplus.data.network

/**
 * URIs used to access the API
 * @author Gabriel Salas.
 */

object Endpoints {
    const val BASE_URL = "http://127.0.0.1:8000/api/"

    // Authentication Endpoints
    const val AUTHENTICATE = "http://ec2-54-78-78-128.eu-west-1.compute.amazonaws.com:80/api/authenticate"
    const val SIGN_UP = "signup"
    const val SIGN_IN = "signin"
    const val UPDATE_PASSWORD = "update-password"

    // User Endpoints
    const val USER_URI = "user/{id}"
    const val SETUP_ACCOUNT = "user/{id}/setup"
    const val GET_COUNTRIES = "https://countriesnow.space/api/v0.1/countries" // Reference: [https://countriesnow.space/api/v0.1/countries]

    // Booking Endpoints
    const val AIRPORTS_SEARCH = "airports/all"
    const val AIRLINES_SEARCH = "airlines/all"
    const val HOTELS_SEARCH = "hotels/all"
    const val BOOKING_SEARCH = "bookingSearch"
    const val GET_FLIGHT_OFFERS = "booking/flights"
    const val GET_HOTEL_OFFERS = "booking/hotels"
    const val GET_USER_BOOKINGS = "bookings/{userId}"
    const val ADD_BOOKING = "booking"
    const val GET_BOOKING = "booking/{id}"
    const val DELETE_BOOKING = "booking/{id}"

    // Admin only Endpoint
    const val GET_ALL_USERS = "admin/users/{orgId}"
    const val ADD_USER = "admin/new-user"
    const val GET_ALL_BOOKINGS = "admin/bookings/{orgId}"




}