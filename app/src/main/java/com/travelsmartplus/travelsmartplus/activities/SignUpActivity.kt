package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.travelsmartplus.travelsmartplus.databinding.ActivitySignUpBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class SignUpActivity : AppCompatActivity() {

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



        }



    }
}