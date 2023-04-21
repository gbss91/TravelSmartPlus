package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.beust.klaxon.Klaxon
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.lang.Exception
import javax.inject.Inject

class AuthServiceImpl @Inject constructor( private val client: OkHttpClient) : AuthService {
    override suspend fun signUp(signUpRequest: SignUpRequest): Response {
        val requestBody = Klaxon().toJsonString(signUpRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(HttpRoutes.SIGN_UP)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                return response
            }
            return response

        } catch (e: Exception) {
            Log.e("AuthService", "Exception: $e")
            throw e
        }
    }

    override suspend fun signIn(signInRequest: SignInRequest): Response {
        val requestBody = Klaxon().toJsonString(signInRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(HttpRoutes.SIGN_IN)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                return response
            }

            return response

        } catch (e: Exception) {
            Log.e("AuthService", "Exception: $e")
            throw e
        }

    }

}