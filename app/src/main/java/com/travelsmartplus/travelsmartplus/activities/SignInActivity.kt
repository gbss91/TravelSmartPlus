package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignInBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * SignInActivity
 * Represents the welcome sign in activity. Allows the user to sign in using email and password.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            signIn()
        }

        binding.signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Observers - observes responses and errors
        authViewModel.signInResponse.observe(this) { response ->
            if (response != null && response.isSuccessful) {
                val accountSetup = response.body()!!.accountSetup
                if (accountSetup) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Avoids returning when pressing back button
                } else {
                    val intent = Intent(this, SetupActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Avoids returning when pressing back button
                }
            } else {
                val error = response?.errorBody()?.string() ?: UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        authViewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                authViewModel.clearError()
            }
        }
    }

    private fun signIn() {

        val email = binding.emailInput
        val password = binding.passwordInput

        // Input validation
        val inputValidation = authViewModel.signInValidation(email, password)

        if (inputValidation) {
            val signInRequest = SignInRequest(email.text.toString(), password.text.toString())
            authViewModel.signIn(signInRequest)
        }
    }
}