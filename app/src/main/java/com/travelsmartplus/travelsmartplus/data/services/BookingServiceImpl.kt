package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.NETWORK_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNEXPECTED_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * BookingServiceImpl
 * Makes booking calls to the API
 *
 * @property bookingService The service for booking API requests using Retrofit.
 * @author Gabriel Salas
 */

class BookingServiceImpl @Inject constructor(
    private val bookingService: BookingService
) : BookingService {

    override suspend fun getAllAirports(): Response<List<Airport>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getAllAirports()
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    is IllegalStateException -> throw NetworkException(UNEXPECTED_ERROR)
                    else -> throw e
                }
            }
        }
    }

    override suspend fun bookingSearch(
        bookingSearchRequest: BookingSearchRequest
    ): Response<Booking> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.bookingSearch(bookingSearchRequest)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    is IllegalStateException -> throw NetworkException(UNEXPECTED_ERROR)
                    else -> throw e
                }
            }
        }
    }
}