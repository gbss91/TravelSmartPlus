package com.travelsmartplus.travelsmartplus.data.services

import android.util.Log
import com.travelsmartplus.travelsmartplus.data.models.Airline
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.data.models.Hotel
import com.travelsmartplus.travelsmartplus.data.models.HotelBooking
import com.travelsmartplus.travelsmartplus.data.models.User
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
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getAllAirlines(): Response<List<Airline>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getAllAirlines()
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getAllHotels(): Response<List<Hotel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getAllHotels()
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun bookingSearch(bookingSearchRequest: BookingSearchRequest): Response<Booking> {
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
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getFlightOffers(bookingSearchRequest: BookingSearchRequest): Response<List<FlightBooking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getFlightOffers(bookingSearchRequest)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }

        }
    }

    override suspend fun getHotelOffers(bookingSearchRequest: BookingSearchRequest): Response<List<HotelBooking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getHotelOffers(bookingSearchRequest)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getUserBookings(userId: String): Response<List<Booking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getUserBookings(userId)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun newBooking(booking: Booking): Response<Booking> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.newBooking(booking)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getBooking(id: String): Response<Booking> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getBooking(id)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun deleteBooking(id: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.deleteBooking(id)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }

    override suspend fun getAllBookings(id: String): Response<List<Booking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingService.getAllBookings(id)
                if (!response.isSuccessful) {
                    Log.e("BookingService", "Error response: ${response.code()} ${response.message()}")
                }
                response
            } catch (e: Exception) {
                Log.e("BookingService", "Exception: ${e.printStackTrace()}")
                when (e) {
                    is IOException -> throw NetworkException(NETWORK_ERROR)
                    else -> throw NetworkException(UNEXPECTED_ERROR)
                }
            }
        }
    }
}