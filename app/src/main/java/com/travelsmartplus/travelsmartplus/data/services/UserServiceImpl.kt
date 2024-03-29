package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.AddUserRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.CountriesResponse
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * UserServiceImpl
 * Makes calls to API to handle user requests
 *
 * @property userService The service handling user API calls using Retrofit.
 * @property sessionManager The session manager for handling user session data.
 * @author Gabriel Salas
 */

class UserServiceImpl @Inject constructor(
    private val userService: UserService,
    private val sessionManager: SessionManager
): UserService {
    override suspend fun getUser(id: String): Response<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.getUser(id)
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun updateUser(id: String, user: User): Response<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.updateUser(id, user)
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun deleteUser(id: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.deleteUser(id)
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun setupAccount(id: String, setupAccountRequest: SetupAccountRequest): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.setupAccount(id, setupAccountRequest)
                if (response.isSuccessful) {
                    sessionManager.saveSetup(true)
                } else {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }

                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getAllUsers(orgId: String): Response<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.getAllUsers(orgId)
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun addUser(newUser: AddUserRequest): Response<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.addUser(newUser)
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getCountries(): Response<CountriesResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.getCountries()
                if (!response.isSuccessful) {
                    Log.e("UserService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("UserService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(ErrorMessages.NETWORK_ERROR)
                    else -> throw NetworkException(ErrorMessages.UNEXPECTED_ERROR)
                }
            }
        }
    }

}