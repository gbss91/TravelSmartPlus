package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse

interface TokenRefreshService {
    suspend fun authenticate(): AuthResponse?

}