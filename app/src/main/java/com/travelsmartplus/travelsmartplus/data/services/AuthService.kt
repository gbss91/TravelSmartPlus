package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import okhttp3.Response

interface AuthService {
    suspend fun signUp(signUpRequest: SignUpRequest): Response
    suspend fun signIn(signInRequest: SignInRequest): Response
}