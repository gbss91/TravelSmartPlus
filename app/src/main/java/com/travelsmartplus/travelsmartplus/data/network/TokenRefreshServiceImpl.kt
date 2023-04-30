package com.travelsmartplus.travelsmartplus.data.network

import android.util.Log
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


class TokenRefreshServiceImpl : TokenRefreshService {

    private val client = OkHttpClient()

    override suspend fun authenticate(): AuthResponse? {
        val request = Request.Builder()
            .url(HttpRoutes.AUTHENTICATE)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                response.use {
                    if (response.isSuccessful) {
                        val objectMapper = jacksonObjectMapper()
                        objectMapper.registerModule(KotlinModule())
                        objectMapper.readValue(response.body?.string(), AuthResponse::class.java)
                    } else {
                        // Handle unsuccessful response
                        Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("AuthService", "Exception: $e")
                null
            }
        }

    }
}