package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.NETWORK_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNEXPECTED_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.io.StringReader
import java.lang.Exception
import java.net.URLDecoder
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
                val responseBody = response.body?.string() // Extract response body
                response.use {
                    if (!response.isSuccessful) {
                        Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                    }

                    response.newBuilder().body(responseBody?.toResponseBody("application/json".toMediaTypeOrNull())).build()
                }
            } catch (e: IOException) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
                throw NetworkException(NETWORK_ERROR)
            } catch (e: IllegalStateException) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
                throw NetworkException(UNEXPECTED_ERROR)
            } catch (e: Exception) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
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
                val responseBody = response.body?.string() // Extract response body
                response.use {
                    // If successful store token and user ID in cookie
                    if (response.isSuccessful ) {
                        val reader = JsonReader(StringReader(responseBody))
                        val authResponse = reader.use { Klaxon().parse<AuthResponse>(it) }

                        // Extract and decode User ID in the cookie
                        val cookie = response.headers("Set-Cookie").find { it.startsWith("user_session=") }
                        val decodedCookie = URLDecoder.decode(cookie, "UTF-8")
                        val encodedUserId = decodedCookie?.substringAfter("userId=")?.substringBefore(";")?.let { URLDecoder.decode(it, "UTF-8") }
                        val userId = encodedUserId?.substring(2)?.toIntOrNull()
                        Log.e("USER ID", "User ID: $userId")

                        if (authResponse != null && userId != null) {
                            sessionManager.saveToken(authResponse.token)
                            sessionManager.saveRefreshToken(authResponse.refreshToken)
                            sessionManager.saveUserId(userId)
                        }

                    } else {
                        Log.e("AuthService", "Error response: ${response.code} ${response.message}")
                    }
                    response.newBuilder().body(responseBody?.toResponseBody("application/json".toMediaTypeOrNull())).build()
                }
            } catch (e: IOException) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
                throw NetworkException(NETWORK_ERROR)
            } catch (e: IllegalStateException) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
                throw NetworkException(UNEXPECTED_ERROR)
            } catch (e: Exception) {
                Log.e("AuthService", "Exception: $e at ${e.fillInStackTrace().stackTrace[0]}")
                throw e
            }
        }
    }
}