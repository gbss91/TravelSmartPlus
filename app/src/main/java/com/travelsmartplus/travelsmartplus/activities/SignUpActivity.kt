package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.remote.models.requests.SignUpRequest
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignUpBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    @Inject lateinit var authService: AuthService
    private lateinit var binding: ActivitySignUpBinding

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

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = authService.signUp(signUpRequest)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val signInRequest = SignInRequest(email.text.toString(), password.text.toString())
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@SignUpActivity, "", Toast.LENGTH_SHORT).show()
                        } else {
                            // Display response message using Toast if sign up fails
                            val message = response.body?.string()
                            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SignUpActivity", "Exception: $e")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, "Unknown error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}