package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignInBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages.UNKNOWN_ERROR
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint

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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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

        val email = binding.email
        val password = binding.password

        // Input validation
        var inputValidation = {
            email.validator().nonEmpty().validEmail().addErrorCallback { email.error = it }.check()
            password.validator().nonEmpty().addErrorCallback { password.error = it }.check()
        }

        if (inputValidation()) {

            val signInRequest = SignInRequest(email.text.toString(), password.text.toString())

            authViewModel.signIn(signInRequest)
        }
    }
}