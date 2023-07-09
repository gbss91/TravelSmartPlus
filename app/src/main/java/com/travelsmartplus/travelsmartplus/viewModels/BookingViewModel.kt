package com.travelsmartplus.travelsmartplus.viewModels

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.data.models.HotelBooking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.NOT_FOUND_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNEXPECTED_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BookingViewModel
 * ViewModel class responsible for managing the booking activities and fragments state and data.
 *
 * @property BookingService The service for booking API requests using Retrofit.
 * @property SessionManager The session manager for handling user session data.
 * @author Gabriel Salas
 */

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingService: BookingService,
    private val sessionManager: SessionManager
): ViewModel() {

    // LiveData object to hold the responses and variables
    private val _airports = MutableLiveData<List<Airport>>()
    private val _booking = MutableLiveData<Booking?>()
    private val _flightOffers = MutableLiveData<List<FlightBooking>>()
    private val _hotelOffers = MutableLiveData<List<HotelBooking>>()
    private val _allBookings = MutableLiveData<List<Booking>>()
    private val _myBookings = MutableLiveData<List<Booking>>()
    private val _errorMessage = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _newSearch = MutableLiveData<Boolean>()
    val airports: LiveData<List<Airport>> = _airports
    val booking: LiveData<Booking?> = _booking
    val flightOffers: LiveData<List<FlightBooking>> = _flightOffers
    val hotelOffers: LiveData<List<HotelBooking>> = _hotelOffers
    val allBookings: LiveData<List<Booking>> = _allBookings
    val myBookings: LiveData<List<Booking>> = _myBookings
    val errorMessage: LiveData<String?> = _errorMessage
    val isLoading: LiveData<Boolean> = _isLoading
    val newSearch: LiveData<Boolean> = _newSearch

    private var bookingSearchRequest: BookingSearchRequest? = null

    // Initiate airportsAutoComplete
    init {
        airportsAutoComplete()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun setBookingSearchRequest(bookingSearchRequest: BookingSearchRequest) {
        this.bookingSearchRequest = bookingSearchRequest
    }

    fun getBookingSearchRequest(): BookingSearchRequest? {
        return bookingSearchRequest
    }

    fun setNewSearch(boolean: Boolean) {
        _newSearch.value = boolean
    }

    fun setBooking(predictedBooking: Booking) {
        _booking.value = predictedBooking
    }

    // Get all airport for autocomplete search
    fun airportsAutoComplete() {
        viewModelScope.launch {
            try {
                val response = bookingService.getAllAirports()
                if (response.isSuccessful) {
                    val airportsList = response.body() ?: emptyList()
                    _airports.postValue(airportsList)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }

    fun bookingSearch() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                bookingSearchRequest?.userId = sessionManager.currentUser()
                val response = bookingService.bookingSearch(bookingSearchRequest!!)
                if (response.code() == 200) {
                    _booking.postValue(response.body())
                } else if (response.code() == 204) {
                    _booking.postValue(null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Get flight offers for manual selection
    fun getFlightOffers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                bookingSearchRequest?.userId = sessionManager.currentUser()
                val response = bookingService.getFlightOffers(bookingSearchRequest!!)
                if (response.isSuccessful)  {
                    val offers = response.body() ?: emptyList()
                    _flightOffers.postValue(offers)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Get hotel offers for manual selection
    fun getHotelOffers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                bookingSearchRequest?.userId = sessionManager.currentUser()
                val response = bookingService.getHotelOffers(bookingSearchRequest!!)
                if (response.isSuccessful) {
                    val offers = response.body() ?: emptyList()
                    _hotelOffers.postValue(offers)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Get current user bookings
    fun getUserBookings() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val userId = sessionManager.currentUser()
                val response = bookingService.getUserBookings(userId.toString())
                if (response.isSuccessful) {
                    val myBookings = response.body() ?: emptyList()
                    _myBookings.postValue(myBookings)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add new booking
    fun addBooking() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val booking = _booking.value ?: throw NotFoundException()
                val response = bookingService.newBooking(booking)
                if (response.isSuccessful) {
                    val newBooking = response.body()
                    _booking.postValue(newBooking)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NotFoundException) {
                e.printStackTrace()
                _errorMessage.postValue(UNEXPECTED_ERROR)
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Get booking using booking ID

    // Delete booking

    // Get all company bookings
    fun getAllBookings() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val orgId = sessionManager.getOrgId()
                val response = bookingService.getAllBookings(orgId.toString())
                if (response.isSuccessful) {
                    val allBookings = response.body() ?: emptyList()
                    _allBookings.postValue(allBookings)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun bookingSearchValidation(
        flightHotel: Boolean,
        oneWay: Boolean,
        departureDate: TextInputEditText,
        returnDate: TextInputEditText,
        checkInDate: TextInputEditText?,
        checkOutDate: TextInputEditText?
    ): Boolean {

        val errorMessages = mutableListOf<String>()

        return try {

            if (!flightHotel) {
                departureDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
                if (!oneWay) {
                    returnDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
                }
            } else {
                departureDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
                if (!oneWay) {
                    returnDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
                }
                checkInDate?.validator()?.nonEmpty()?.addRule(NotBlankRule())?.addErrorCallback { errorMessages.add(it) }?.check()
                checkOutDate?.validator()?.nonEmpty()?.addRule(NotBlankRule())?.addErrorCallback { errorMessages.add(it) }?.check()
            }

            if (errorMessages.isNotEmpty()) {
                return false
            }
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue(UNKNOWN_ERROR)
            false
        }
    }

}