package com.travelsmartplus.travelsmartplus.data.network

import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse

interface TokenRefreshService {
    suspend fun authenticate(userId: Int?, refreshToken: String): AuthResponse?

}