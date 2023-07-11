package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.AddUserRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.UpdatePasswordRequest
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNEXPECTED_ERROR
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * UserViewModel
 * ViewModel class responsible for user management.
 *
 * @property authService The AuthService instance for handling authentication API calls.
 * @property userService The service handling user API calls.
 * @property sessionManager The SessionManager instance for handling sessions.
 * @author Gabriel Salas
 */

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authService: AuthService,
    private val userService: UserService,
    private val sessionManager: SessionManager
): ViewModel() {

    // LiveData objects to hold the responses - Public val are readOnly
    private val _userData = MutableLiveData<User>()
    private val _users = MutableLiveData<List<User>>()
    private val _passwordResponse = MutableLiveData<Response<Unit>>()
    private val _deleteUserResponse = MutableLiveData<Response<Unit>>()
    private val _countries = MutableLiveData<List<String>>()
    private val _errorMessage = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _addEditSuccessful = MutableLiveData<Boolean>()
    val userData: LiveData<User> = _userData
    val users: LiveData<List<User>> = _users
    val passwordResponse: LiveData<Response<Unit>> = _passwordResponse
    val deleteUserResponse: LiveData<Response<Unit>> = _deleteUserResponse
    val countries: LiveData<List<String>> = _countries
    val errorMessage: LiveData<String?> = _errorMessage
    val isLoading: LiveData<Boolean> = _isLoading
    val addEditSuccessful: LiveData<Boolean> = _addEditSuccessful

    fun clearError() {
        _errorMessage.value = null
    }

    fun getCurrentUser(): Int {
        return sessionManager.currentUser()
    }

    fun isAdmin(): Boolean {
        return sessionManager.admin()
    }

    // Gets specific user details
    fun getUser(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userService.getUser(userId)
                if (response.isSuccessful) {
                    val userData = response.body()
                    _userData.postValue(userData ?: throw NullPointerException("Get User: User data is null"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                _errorMessage.postValue(UNEXPECTED_ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Get all users for company
    fun getAllUsers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val orgId = sessionManager.getOrgId()
                val response = userService.getAllUsers(orgId.toString())
                if (response.isSuccessful) {
                    val userList = response.body() ?: emptyList()
                    _users.postValue(userList)
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

    // Updates current user details
    fun updateUser(userId: String, updatedUser: User) {
        _addEditSuccessful.value = false
        viewModelScope.launch {
            try {
                val response = userService.updateUser(userId, updatedUser)
                if (response.isSuccessful) {
                    val userData = response.body()
                    _addEditSuccessful.value = true
                    _userData.postValue(userData ?: throw NullPointerException("Update User: User data is null"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                _errorMessage.postValue(UNEXPECTED_ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _addEditSuccessful.value = false
            }
        }

    }

    fun passwordUpdate(updatedPassword: UpdatePasswordRequest) {
        viewModelScope.launch {
            try {
                val response = authService.updatePassword(updatedPassword)
                _passwordResponse.postValue(response)
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            }
        }
    }

    // Deletes user
    fun deleteUser(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userService.deleteUser(userId)
                _deleteUserResponse.postValue(response)
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

    // Adds new user
    fun addUser(newUser: AddUserRequest) {
        _addEditSuccessful.value = false
        viewModelScope.launch {
            try {
                val response = userService.addUser(newUser)
                if (response.isSuccessful) {
                    val user = response.body() ?: throw NullPointerException("Add User: User data is null")
                    _addEditSuccessful.value = true
                    _userData.postValue(user)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }

            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                _errorMessage.postValue(UNEXPECTED_ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _addEditSuccessful.value = false
            }
        }
    }

    // Get CountriesResponse list for travel data
    fun getCountries() {
        viewModelScope.launch {
            try {
                val response = userService.getCountries()
                if (response.isSuccessful) {
                    val countriesResponse = response.body()
                    val countriesList = countriesResponse?.data?.map { it.country } ?: emptyList()
                    _countries.postValue(countriesList)
                } else {
                    _errorMessage.postValue(UNKNOWN_ERROR)
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

    // Clear current user when signing out
    fun signOut() {
        sessionManager.saveCurrentUser(-1)
    }

    fun editedDetailsValidation(firstName: TextInputEditText, lastName: TextInputEditText, email: TextInputEditText,): Boolean {
        return try {
            firstName.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { firstName.error = it }.check()
            lastName.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { lastName.error = it }.check()
            email.validator().nonEmpty().addRule(NotBlankRule()).validEmail().addErrorCallback { email.error = it }.check()

            // Return if no errors
            firstName.error == null &&
                    lastName.error == null &&
                    email.error == null

        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue(UNKNOWN_ERROR)
            false
        }
    }

    fun editTravelDetailsValidation(
        dob: TextInputEditText,
        passportNumber: TextInputEditText,
        expiryDate: TextInputEditText
    ): Boolean {

        val errorMessages = mutableListOf<String>()

        return try {
            dob.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            passportNumber.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()
            expiryDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { errorMessages.add(it) }.check()

            // Return if no errors
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
            _errorMessage.postValue(UNKNOWN_ERROR)
            false
        }
    }

}