package com.travelsmartplus.travelsmartplus.viewModels

import android.widget.AutoCompleteTextView
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
    private val _predictedBooking = MutableLiveData<Booking?>()
    private val _flightOffers = MutableLiveData<List<FlightBooking>>()
    private val _hotelOffers = MutableLiveData<List<HotelBooking>>()
    private val _errorMessage = MutableLiveData<String>()
    val airports: LiveData<List<Airport>> = _airports
    val predictedBooking: LiveData<Booking?> = _predictedBooking
    val flightOffers: LiveData<List<FlightBooking>> = _flightOffers
    val hotelOffers: LiveData<List<HotelBooking>> = _hotelOffers
    val errorMessage: LiveData<String> = _errorMessage

    private var bookingSearchRequest: BookingSearchRequest? = null

    // Initiate airportsAutoComplete
    init {
        airportsAutoComplete()
    }

    fun setBookingSearchRequest(bookingSearchRequest: BookingSearchRequest) {
        this.bookingSearchRequest = bookingSearchRequest
    }

    fun getBookingSearchRequest(): BookingSearchRequest? {
        return bookingSearchRequest
    }

    private fun airportsAutoComplete() {
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
        viewModelScope.launch {
            try {
                bookingSearchRequest?.userId = sessionManager.currentUser()
                val response = bookingService.bookingSearch(bookingSearchRequest!!)
                if (response.code() == 200 ) {
                    _predictedBooking.postValue(response.body())
                } else if (response.code() == 204 ) {
                    _predictedBooking.postValue(null)
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

    fun bookingSearchValidation(
        flightOnly: Boolean,
        oneWay: Boolean,
        originCity: AutoCompleteTextView,
        destinationCity: AutoCompleteTextView,
        departureDate: TextInputEditText,
        returnDate: TextInputEditText,
        checkInDate: TextInputEditText?,
        checkOutDate: TextInputEditText?
    ): Boolean {

        val errorMessages = mutableListOf<String>()

        return try {
            originCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            destinationCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()

            if (flightOnly) {
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

            // Display single toast if any validation errors
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