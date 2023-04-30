package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.NETWORK_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNEXPECTED_ERROR
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.URLDecoder
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : AuthService {
    override suspend fun signUp(signUpRequest: SignUpRequest): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.signUp(signUpRequest)
                if (!response.isSuccessful) {
                    Log.e("AuthService", "Error response: ${response.code()} ${response.message()}")
                }
                response
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

    override suspend fun signIn(signInRequest: SignInRequest): Response<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.signIn(signInRequest)
                val responseBody = response.body()

                if (response.isSuccessful) {
                    // Extract and decode User ID in the cookie
                    val cookie = response.headers()["Set-Cookie"]
                    val decodedCookie = URLDecoder.decode(cookie, "UTF-8")
                    val encodedUserId = decodedCookie?.substringAfter("userId=")?.substringBefore(";")?.let { URLDecoder.decode(it, "UTF-8") }
                    val userId = encodedUserId?.substring(2)?.toIntOrNull()
                    Log.e("USER ID", "User ID: $userId")

                    if (responseBody != null && userId != null) {
                        sessionManager.saveToken(responseBody.token)
                        sessionManager.saveRefreshToken(responseBody.refreshToken)
                        sessionManager.saveUserId(userId)
                    }
                } else {
                    Log.e("AuthService", "Error response: ${response.code()} ${response.message()}")
                }
                response

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