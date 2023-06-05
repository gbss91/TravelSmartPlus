package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.databinding.ActivitySetupPasswordBinding
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
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
            updatePassword()
        }

        // Observer - observes errors from View Model
        setupViewModel.errorMessage.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updatePassword() {

        val password = binding.setupPasswordInput
        val confirmPass = binding.setupConfirmPasswordInput

        // Input validation
        val inputValidation = setupViewModel.passwordValidation(password, confirmPass)

        // If valid start new activity and sent password
        if (inputValidation) {
            val intent = Intent(this, SetupPreferencesActivity::class.java)
            intent.putExtra("newPass", password.text.toString())
            startActivity(intent)
        }
    }
}