package com.travelsmartplus.travelsmartplus.data.network

import com.travelsmartplus.travelsmartplus.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * AuthInterceptor
 * Intercepts the request chain and adds the JWT token to the request header.
 * If the response is unauthorized, it attempts to refresh the token and retry the request.
 *
 * @return The response from the server
 * @author Gabriel Salas
 */

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

            // Close previous response
            response.close()

            // Retry request with refresh token
            val newRequest = originalRequest
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer $refreshToken").build()
            val newResponse = chain.proceed(newRequest)

            // Refresh tokens if successful, otherwise clear session to log user out
            if (newResponse.isSuccessful) {
                scope.launch {
                    refreshTokens(sessionManager.currentUser(), refreshToken)
                }
            } else {
                sessionManager.clearSession()
            }
            return newResponse
        }
        return response
    }

    private val tokenRefreshService = TokenRefreshServiceImpl()

    // Call to refresh tokens
    private suspend fun refreshTokens(userId: Int?, refreshToken: String) {
        val authResponse = tokenRefreshService.authenticate(userId, refreshToken)
        if (authResponse != null) {
            sessionManager.saveToken(authResponse.token)
            sessionManager.saveRefreshToken(authResponse.refreshToken)
        }
    }
}