package com.travelsmartplus.travelsmartplus.data.services

import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.AddUserRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.CountriesResponse
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.ADD_USER
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_ALL_USERS
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.GET_COUNTRIES
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.SETUP_ACCOUNT
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.USER_URI
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET(USER_URI)
    suspend fun getUser(@Path("id") id: String): Response<User>

    @PUT(USER_URI)
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE(USER_URI)
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>

    @POST(SETUP_ACCOUNT)
    suspend fun setupAccount(@Path("id") id: String, @Body setupAccountRequest: SetupAccountRequest): Response<Unit>

    @GET(GET_ALL_USERS)
    suspend fun getAllUsers(@Path("orgId") orgId: String): Response<List<User>>

    @POST(ADD_USER)
    suspend fun addUser(@Body newUser: AddUserRequest): Response<User>

    @GET(GET_COUNTRIES)
    suspend fun getCountries(): Response<CountriesResponse>

}