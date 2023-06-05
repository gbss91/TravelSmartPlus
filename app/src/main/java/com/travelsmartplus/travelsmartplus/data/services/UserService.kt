package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.SETUP_ACCOUNT
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.USER_URI
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET(USER_URI)
    suspend fun getUser(@Path("id") id: String): Response<User>

    @POST(USER_URI)
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<Unit>

    @DELETE(USER_URI)
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>

    @POST(SETUP_ACCOUNT)
    suspend fun setupAccount(@Path("id") id: String, @Body setupAccountRequest: SetupAccountRequest): Response<Unit>
}