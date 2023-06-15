package com.travelsmartplus.travelsmartplus.data.network

object HttpRoutes {
    const val BASE_URL = "http://10.0.2.2:8000/api/"
    const val AUTHENTICATE = "http://10.0.2.2:8000/api/authenticate"
    const val SIGN_UP = "signup"
    const val SIGN_IN = "signin"
    const val UPDATE_PASSWORD = "update-password"

    // User Routes
    const val USER_URI = "user/{id}"
    const val SETUP_ACCOUNT = "user/{id}/setup"

    // Booking Routes
    const val AIRPORTS_SEARCH = "airports/all"
    const val BOOKING_SEARCH = "bookingSearch"
    const val GET_ALL_BOOKINGS = "bookings/{orgId}"
    const val GET_USER_BOOKINGS = "bookings/{userId}"
    const val ADD_BOOKING = "booking"
    const val GET_BOOKING = "booking/{userId}"
    const val DELETE_BOOKING = "booking/{userId}"
    const val GET_FLIGHT_OFFERS = "booking/flights"
    const val GET_HOTEL_OFFERS = "booking/hotels"


}