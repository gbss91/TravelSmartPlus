package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.network.NetworkException
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.SessionManager
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
    private val _userProfile = MutableLiveData<User>()
    private val _specificUser = MutableLiveData<User>()
    private val _users = MutableLiveData<List<User>>()
    private val _updatedUser = MutableLiveData<Response<User>>()
    private val _deleteUserResponse = MutableLiveData<Response<Unit>>()
    private val _errorMessage = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>()
    val userProfile: LiveData<User> = _userProfile
    val specificUser: LiveData<User> = _specificUser
    val users: LiveData<List<User>> = _users
    val updatedUser: LiveData<Response<User>> = _updatedUser
    val deleteUserResponse: LiveData<Response<Unit>> = _deleteUserResponse
    val errorMessage: LiveData<String?> = _errorMessage
    val isLoading: LiveData<Boolean> = _isLoading

    fun clearError() {
        _errorMessage.value = null
    }

    // Gets current user data
    fun getCurrentUser() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val currentUser = sessionManager.currentUser()
                val response = userService.getUser(currentUser.toString())
                if (response.isSuccessful) {
                    val userData = response.body()
                    _userProfile.postValue(userData ?: throw NullPointerException("User data is null"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Gets specific user details
    fun getUser(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userService.getUser(userId)
                if (response.isSuccessful) {
                    val userData = response.body()
                    _userProfile.postValue(userData ?: throw NullPointerException("User data is null"))
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: UNKNOWN_ERROR)
                }
            } catch (e: NetworkException) {
                e.printStackTrace()
                _errorMessage.postValue(e.message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                _errorMessage.postValue(UNKNOWN_ERROR)
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
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userService.updateUser(userId, updatedUser)

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
    fun addUser(newUser: User) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userService.addUser(newUser)
                if (response.isSuccessful) {
                    val user = response.body()
                    // Handle user
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

    // Clear current user when signing out
    fun signOut() {
        sessionManager.saveCurrentUser(-1)
    }


}