package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignInBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
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
            val accountSetup = response.body()!!.accountSetup
            if (response != null && response.isSuccessful) {
                if (accountSetup) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, SetupWelcomeActivity::class.java)
                    startActivity(intent)
                }
            } else {
                val error = response?.errorBody()?.string() ?: UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        authViewModel.errorMessage.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun signIn() {

        val email = binding.emailInput
        val password = binding.passwordInput

        // Input validation
        val inputValidation = {
            email.validator().nonEmpty().addRule(NotBlankRule()).validEmail().addErrorCallback { email.error = it }.check()
            password.validator().nonEmpty().addRule(NotBlankRule()).addRule(NotBlankRule()).addErrorCallback { password.error = it }.check()

            // Return if not errors
            email.error == null &&  password.error == null
        }

        if (inputValidation()) {
            val signInRequest = SignInRequest(email.text.toString(), password.text.toString())
            authViewModel.signIn(signInRequest)
        }
    }
}