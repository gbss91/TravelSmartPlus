package com.travelsmartplus.travelsmartplus.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * BookingViewModel
 * ViewModel class responsible for managing the booking activities and fragments state and data.
 *
 * @author Gabriel Salas
 */

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    // LiveData object to hold the responses and variables
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun bookingSearchValidation(
        flightOnly: Boolean,
        originCity: TextInputEditText,
        destinationCity: TextInputEditText,
        departureDate: TextInputEditText,
        returnDate: TextInputEditText,
        checkInDate: TextInputEditText?,
        checkOutDate: TextInputEditText?

    ): Boolean {

        val errorMessages = mutableListOf<String>()

        return try {
            originCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            destinationCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            departureDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            if (flightOnly) {
                returnDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            } else {
                checkInDate?.validator()?.nonEmpty()?.addRule(NotBlankRule())?.addErrorCallback { errorMessages.add(it) }?.check()
                checkOutDate?.validator()?.nonEmpty()?.addRule(NotBlankRule())?.addErrorCallback { errorMessages.add(it)}?.check()
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