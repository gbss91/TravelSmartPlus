package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignUpBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * SignUpActivity
 * Represents the welcome sign up activity. Allows the user to create a new account.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            signUp()
        }

        binding.signInLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Observers - observes responses and errors
        authViewModel.signUpResponse.observe(this) { response ->
            if (response != null && !response.isSuccessful) {
                val error = response.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        authViewModel.signInResponse.observe(this) { response ->
            if (response != null && response.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish() // Avoids returning when pressing back button
            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        authViewModel.errorMessage.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun signUp() {

        val firstName = binding.firstNameInput
        val lastName = binding.lastNameInput
        val email = binding.emailInput
        val password = binding.passwordInput
        val confirmPass = binding.confirmPassInput
        val companyName = binding.companyNameInput
        val duns = binding.dunsInput

        // Input validation
        val inputValidation = authViewModel.signUpValidation(firstName, lastName, email, companyName, duns, password, confirmPass)

        if(inputValidation) {

            val signUpRequest = SignUpRequest(
                firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                password.text.toString(),
                companyName.text.toString(),
                duns.text.toString().toInt()
            )

            val signInRequest = SignInRequest(email.text.toString(), password.text.toString())
            authViewModel.signUpAndSignIn(signUpRequest, signInRequest)
        }
    }
}