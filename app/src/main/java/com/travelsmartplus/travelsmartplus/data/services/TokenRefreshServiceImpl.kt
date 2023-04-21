package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.beust.klaxon.Klaxon
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception


class TokenRefreshServiceImpl : TokenRefreshService {

    private val client = OkHttpClient()

    override suspend fun authenticate(): AuthResponse? {
        val request = Request.Builder()
            .url(HttpRoutes.AUTHENTICATE)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Klaxon().parse<AuthResponse>(response.body.toString())
            } else {
                // Handle unsuccessful response
                Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                null
            }
        } catch (e: Exception) {
            // Handle exception
            Log.e("AuthService", "Exception: $e")
            null
        }
    }
}