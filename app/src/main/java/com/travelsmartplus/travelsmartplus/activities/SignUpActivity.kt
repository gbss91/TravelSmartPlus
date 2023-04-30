package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignUpBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint

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
            if (response != null && response.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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

        val firstName = binding.firstName
        val lastName = binding.lastName
        val email = binding.email
        val password = binding.password
        val confirmPass = binding.confirmPass
        val companyName = binding.companyName
        val duns = binding.duns

        // Input validation
        var inputValidation = {
            firstName.validator().nonEmpty().addErrorCallback { firstName.error = it }.check()
            lastName.validator().nonEmpty().addErrorCallback { lastName.error = it }.check()
            email.validator().nonEmpty().validEmail().addErrorCallback { email.error = it }.check()
            companyName.validator().nonEmpty().addErrorCallback { companyName.error = it }.check()
            duns.validator().nonEmpty().addErrorCallback { duns.error = it }.check()

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
        }

        if(inputValidation()) {

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