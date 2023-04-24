package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.services.AuthService
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
    val authResponse: LiveData<Response> = _authResponse

    // Sign up using coroutine
    fun signUp(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            val response = authService.signUp(signUpRequest)
            _authResponse.postValue(response)
        }
    }

    // Sign in using coroutine
    fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            val response = authService.signIn(signInRequest)
            _authResponse.postValue(response)
        }
    }

    // Sign up and sign in using coroutine
    fun signUpAndSignIn(signUpRequest: SignUpRequest, signInRequest: SignInRequest) {
        viewModelScope.launch {
            val signUpResponse = authService.signUp(signUpRequest)
            if (signUpResponse.isSuccessful) {
                val signInResponse = authService.signIn(signInRequest)
                _authResponse.postValue(signInResponse)
            } else {
                _authResponse.postValue(signUpResponse)
            }
        }
    }


}