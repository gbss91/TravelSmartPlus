package com.travelsmartplus.travelsmartplus.data.network

import com.travelsmartplus.travelsmartplus.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
)  : Interceptor {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()
        val originalRequest = chain.request().newBuilder()

        // Add JWT token to request
        if (token != null) {
            originalRequest.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(originalRequest.build())

        // Retry with refresh token if unauthorised
        if (response.code == 401) {
            // If no refresh token available, return original response
            val refreshToken = sessionManager.getRefreshToken() ?: return response

            response.close()

            val newRequest = originalRequest.addHeader("Authorization", "Bearer $refreshToken").build()
            val newResponse = chain.proceed(newRequest)
            // Refresh tokens if refresh token is valid and request is successful, else return unauthorised response
            if (newResponse.isSuccessful) {
                scope.launch {
                    refreshTokens()
                }
            }
            return newResponse
        }
        return response
    }

    private val tokenRefreshService = TokenRefreshServiceImpl()
    // Call to refresh tokens
    private suspend fun refreshTokens() {
        val authResponse = tokenRefreshService.authenticate()
        if (authResponse != null) {
            sessionManager.saveToken(authResponse.token)
            sessionManager.saveRefreshToken(authResponse.refreshToken)
        }
    }
}