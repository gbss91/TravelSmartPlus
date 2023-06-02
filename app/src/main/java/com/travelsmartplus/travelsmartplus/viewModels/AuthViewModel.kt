package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
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

    // LiveData objects to hold the responses
    private val _signUpResponse = MutableLiveData<Response<Unit>>()
    private val _signInResponse = MutableLiveData<Response<AuthResponse>>()
    private val _errorMessage = MutableLiveData<String>()
    val signUpResponse: LiveData<Response<Unit>> = _signUpResponse
    val signInResponse: LiveData<Response<AuthResponse>> = _signInResponse
    val errorMessage: LiveData<String> = _errorMessage

    // Sign in using coroutine
    fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                val response = authService.signIn(signInRequest)
                _signInResponse.postValue(response)
            } catch (e: NetworkException) {
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
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
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }

}