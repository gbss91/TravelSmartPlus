package com.travelsmartplus.travelsmartplus.data.network

object HttpRoutes {
    const val BASE_URL = "http://10.0.2.2:8000/api/"
    const val AUTHENTICATE = "http://10.0.2.2:8000/api/authenticate"
    const val SIGN_UP = "signup"
    const val SIGN_IN = "signin"
    const val UPDATE_PASSWORD = "update-password"

    const val USER_URI = "user/{id}"
    const val SETUP_ACCOUNT = "user/{id}/setup"

}