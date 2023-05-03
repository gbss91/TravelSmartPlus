package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.SIGN_IN
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.SIGN_UP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(SIGN_UP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<Unit>

    @POST(SIGN_IN)
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<AuthResponse>
}