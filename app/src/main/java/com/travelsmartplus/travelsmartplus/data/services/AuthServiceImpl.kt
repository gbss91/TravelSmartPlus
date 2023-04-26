package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.beust.klaxon.Klaxon
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.network.SessionManager
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val client: OkHttpClient,
    private val sessionManager: SessionManager
) : AuthService {
    override suspend fun signUp(signUpRequest: SignUpRequest): Response {
        val requestBody = Klaxon().toJsonString(signUpRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(HttpRoutes.SIGN_UP)
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                }
                response

            } catch (e: IOException) {
                Log.e("AuthService", "Exception: $e")
                throw NetworkException("Network error occurred. Please try again later!")
            } catch (e: Exception) {
                Log.e("AuthService", "Exception: $e")
                throw e
            }
        }
    }

    override suspend fun signIn(signInRequest: SignInRequest): Response {
        val requestBody = Klaxon().toJsonString(signInRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(HttpRoutes.SIGN_IN)
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()

                // If successful store token and user ID in cookie
                if (response.isSuccessful ) {
                    val authResponse = Klaxon().parse<AuthResponse>(response.body.toString())
                    val cookie = response.headers("Set-Cookie").find { it.startsWith("user_session=") }

                    if (authResponse != null && cookie != null) {
                        sessionManager.saveToken(authResponse.token)
                        sessionManager.saveRefreshToken(authResponse.refreshToken)
                        sessionManager.saveUserId(cookie.toInt())
                    }

                } else {
                    Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                }
                response

            } catch (e: IOException) {
                Log.e("AuthService", "Exception: $e")
                throw NetworkException("Network error occurred. Please try again later!")
            } catch (e: Exception) {
                Log.e("AuthService", "Exception: $e")
                throw e
            }
        }
    }
}