package com.travelsmartplus.travelsmartplus.data.network

object HttpRoutes {
    private const val BASE_URL = "http://10.0.2.2:8000/api"
    const val SIGN_UP = "$BASE_URL/signup"
    const val SIGN_IN = "$BASE_URL/signin"
    const val AUTHENTICATE = "$BASE_URL/authenticate"
}