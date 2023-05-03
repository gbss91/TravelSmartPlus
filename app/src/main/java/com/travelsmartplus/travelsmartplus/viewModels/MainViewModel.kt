package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel()  {

    // LiveData object to hold the responses and variables
    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean> = _isSignedIn

    init {
        // Set the initial value of the signedIn variable
        _isSignedIn.value = sessionManager.currentUser() != -1
    }

}