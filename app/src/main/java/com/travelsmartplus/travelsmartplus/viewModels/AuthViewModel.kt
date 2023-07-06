package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * AuthViewModel
 * ViewModel class responsible for handling authentication-related operations.
 *
 * @property authService The AuthService instance for performing authentication API calls.
 * @author Gabriel Salas
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    // LiveData objects to hold the responses and variables - Public val are readOnly
    private val _signUpResponse = MutableLiveData<Response<Unit>>()
    private val _signInResponse = MutableLiveData<Response<AuthResponse>>()
    private val _errorMessage = MutableLiveData<String?>()
    val signUpResponse: LiveData<Response<Unit>> = _signUpResponse
    val signInResponse: LiveData<Response<AuthResponse>> = _signInResponse
    val errorMessage: LiveData<String?> = _errorMessage

    fun clearError() {
        _errorMessage.value = null
    }

    // Sign in using coroutine
    fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                val response = authService.signIn(signInRequest)
                _signInResponse.postValue(response)
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }

    // Sign up and sign in using coroutine
    fun signUpAndSignIn(signUpRequest: SignUpRequest, signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                val signUpResponse = authService.signUp(signUpRequest)
                if (signUpResponse.isSuccessful) {
                    val signInResponse = authService.signIn(signInRequest)
                    _signInResponse.postValue(signInResponse)
                } else {
                    _signUpResponse.postValue(signUpResponse)
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

    // Validates sign up input
    fun signUpValidation(
        firstName: TextInputEditText,
        lastName: TextInputEditText,
        email: TextInputEditText,
        companyName: TextInputEditText,
        duns: TextInputEditText,
        password: TextInputEditText,
        confirmPass: TextInputEditText
    ): Boolean {

        return try {
            firstName.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { firstName.error = it }.check()
            lastName.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { lastName.error = it }.check()
            email.validator().nonEmpty().addRule(NotBlankRule()).validEmail().addErrorCallback { email.error = it }.check()
            companyName.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { companyName.error = it }.check()
            duns.validator().nonEmpty().addRule(NotBlankRule()).validNumber().addErrorCallback { duns.error = it }.check()

            password.validator()
                .nonEmpty()
                .atleastOneUpperCase()
                .atleastOneLowerCase()
                .atleastOneNumber()
                .minLength(8)
                .addErrorCallback { password.error = it }
                .check()

            confirmPass.validator()
                .textEqualTo(password.text.toString())
                .addErrorCallback { confirmPass.error = "Password doesn't match" }
                .check()

            // Return if no errors
            firstName.error == null &&
                    lastName.error == null &&
                    email.error == null &&
                    companyName.error == null &&
                    duns.error == null &&
                    password.error == null &&
                    confirmPass.error == null

        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue(UNKNOWN_ERROR)
            false
        }
    }

    fun signInValidation(
        email: TextInputEditText,
        password: TextInputEditText
    ): Boolean {
        return try {
            email.validator().nonEmpty().addRule(NotBlankRule()).validEmail().addErrorCallback { email.error = it }.check()
            password.validator().nonEmpty().addRule(NotBlankRule()).addRule(NotBlankRule()).addErrorCallback { password.error = it }.check()

            // Return if no errors
            email.error == null && password.error == null
        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue(ErrorMessages.UNKNOWN_ERROR)
            false
        }
    }

}