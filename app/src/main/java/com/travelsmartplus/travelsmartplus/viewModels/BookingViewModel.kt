package com.travelsmartplus.travelsmartplus.viewModels

import android.widget.AutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.Airport
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
 * @author Gabriel Salas
 */

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingService: BookingService,
    private val sessionManager: SessionManager
): ViewModel() {

    // LiveData object to hold the responses and variables
    private val _airports = MutableLiveData<List<Airport>>()
    private val _errorMessage = MutableLiveData<String>()
    val airports: LiveData<List<Airport>> = _airports
    val errorMessage: LiveData<String> = _errorMessage

    // Initiate airportsAutoComplete
    init {
        airportsAutoComplete()
    }

    private fun airportsAutoComplete() {
        viewModelScope.launch {
            try {
                val response = bookingService.getAllAirports()
                if (response.isSuccessful) {
                    val airportsList = response.body() ?: emptyList()
                    _airports.postValue(airportsList)
                } else {
                    _errorMessage.postValue(response.errorBody().toString())
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