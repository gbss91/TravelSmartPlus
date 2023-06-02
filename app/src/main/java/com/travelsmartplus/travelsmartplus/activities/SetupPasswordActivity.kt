package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.ActivitySetupPasswordBinding
import com.travelsmartplus.travelsmartplus.utils.NotBlankRule
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupPasswordActivity
 * Represents the password setup activity. Lets a new user to update their password.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupPasswordBinding
    private val setupViewModel: SetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setupPassBtn.setOnClickListener {
            //val intent = Intent(this, )
        }

        // Observers - observes responses and errors
    }

    private fun updatePassword() {

        val pass = binding.setupPasswordInput
        val confirmPass = binding.setupConfirmPasswordInput

        // Input validation
        val inputValidation = {
            pass.validator()
                .nonEmpty()
                .atleastOneUpperCase()
                .atleastOneLowerCase()
                .atleastOneNumber()
                .minLength(8)
                .addErrorCallback { pass.error = it }
                .check()

            confirmPass.validator()
                .textEqualTo(confirmPass.text.toString())
                .addErrorCallback { confirmPass.error = "Password doesn't match" }
                .check()

            pass.error == null && confirmPass.error == null
        }

        if (inputValidation()) {


        }
    }
}