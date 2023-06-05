package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * SetupViewModel
 * ViewModel class responsible for managing the setup process.
 *
 * @property authService The AuthService instance for handling authentication API calls.
 * @property userService The service handling user API calls.
 * @property sessionManager The SessionManager instance for handling sessions. .
 * @author Gabriel Salas
 */

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val authService: AuthService,
    private val userService: UserService,
    private val sessionManager: SessionManager
): ViewModel() {

    // LiveData objects to hold the responses - Public val are readOnly
    private val _setupAccountResponse = MutableLiveData<Response<Unit>>()
    private val _errorMessage = MutableLiveData<String>()
    val setupAccountResponse: LiveData<Response<Unit>> = _setupAccountResponse
    val errorMessage: LiveData<String> = _errorMessage


    fun setupAccount(setupAccountRequest: SetupAccountRequest) {
        viewModelScope.launch {
            try {
                val id = sessionManager.currentUser().toString()
                val response = userService.setupAccount(id, setupAccountRequest)
                _setupAccountResponse.postValue(response)
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(ErrorMessages.UNKNOWN_ERROR)
            }
        }
    }

    fun passwordValidation(password: TextInputEditText, confirmPass: TextInputEditText): Boolean {
        return try {
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

            password.error == null && confirmPass.error == null

        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue(ErrorMessages.UNKNOWN_ERROR)
            false
        }
    }
     fun checkBoxValidation(selectedCheckboxes: Set<String>): Boolean {
         return try {
             selectedCheckboxes.isNotEmpty()
         } catch (e: Exception) {
             e.printStackTrace()
             _errorMessage.postValue(ErrorMessages.UNKNOWN_ERROR)
             false
         }
     }

}