package com.travelsmartplus.travelsmartplus.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    // LiveData object to hold the responses
    private val _authResponse = MutableLiveData<Response>()
    private val _errorMessage = MutableLiveData<String>()
    val authResponse: LiveData<Response> = _authResponse
    val errorMessage: LiveData<String> = _errorMessage

    // Sign up using coroutine
    fun signUp(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            try {
                val response = authService.signUp(signUpRequest)
                _authResponse.postValue(response)
            } catch (e: NetworkException) {
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }

    // Sign in using coroutine
    fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                val response = authService.signIn(signInRequest)
                _authResponse.postValue(response)
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
                    _authResponse.postValue(signInResponse)
                } else {
                    _authResponse.postValue(signUpResponse)
                }
            } catch (e: NetworkException) {
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }


}