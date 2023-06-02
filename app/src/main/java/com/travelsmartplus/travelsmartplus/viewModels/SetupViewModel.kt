package com.travelsmartplus.travelsmartplus.viewModels

import androidx.lifecycle.ViewModel
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * SetupViewModel
 * ViewModel class responsible for managing the setup process.
 *
 * @property authService The AuthService instance for handling authentication API calls.
 * @property sessionManager The SessionManager instance for handling sessions. .
 * @author Gabriel Salas
 */

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager
): ViewModel() {


}