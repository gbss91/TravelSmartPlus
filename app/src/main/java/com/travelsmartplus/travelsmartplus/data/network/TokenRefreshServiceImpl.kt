package com.travelsmartplus.travelsmartplus.data.network

import android.util.Log
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * TokenRefreshServiceImpl
 * Makes the call to authenticate and refresh tokens. Implements [TokenRefreshService]
 *
 * @return @return The authentication response containing the new tokens, or null if the operation fails.
 * @author Gabriel Salas
 */

class TokenRefreshServiceImpl : TokenRefreshService {

    private val client = OkHttpClient()

    override suspend fun authenticate(userId: Int?, refreshToken: String): AuthResponse? {
        val cookie = Cookie.Builder()
            .name("user_id")
            .value(userId.toString())
            .domain("127.0.0.1")
            .build()

        val request = Request.Builder()
            .url(Endpoints.AUTHENTICATE)
            .addHeader("Authorization", "Bearer $refreshToken")
            .addHeader("Cookie", cookie.toString())
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (response.isSuccessful) {
                        val json = response.body?.string()
                        json?.let { Json.decodeFromString<AuthResponse>(it) }
                    } else {
                        Log.e("TokenRefreshService", "Unable to refresh token. Error response: ${response.code} ${response.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e("TokenRefreshService", "Exception: $e")
                null
            }
        }

    }
}