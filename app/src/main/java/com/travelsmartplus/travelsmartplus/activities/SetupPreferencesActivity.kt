package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.databinding.ActivitySetupPreferencesBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupPreferencesActivity
 * Represents the preferences setup activity. Lets a new user to add preferences to their account.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupPreferencesBinding
    private val setupViewModel: SetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get password from previous SetupPasswordActivity
        val newPass = intent.getStringExtra("newPass")!!

        binding.preferencesSetupBtn.setOnClickListener {
            setupAccount(newPass)
        }

        // Observer - observes responses and errors from View Model
        setupViewModel.errorMessage.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }

        setupViewModel.setupAccountResponse.observe(this) { response ->
            if (response != null && response.isSuccessful) {
                val intent = Intent(this, SetupFinishActivity::class.java)
                startActivity(intent)
            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun selectedCheckboxes(gridLayout: GridLayout): Set<String> {
        val selectedCheckboxes = mutableSetOf<CheckBox>()
        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView is CheckBox && childView.isChecked) {
                selectedCheckboxes.add(childView)
            }
        }

        return selectedCheckboxes.map { it.tag as String }.toSet()
    }

    private fun setupAccount(newPassword: String) {
        // Get selected checkboxes
        val preferredAirlines = selectedCheckboxes(binding.airlineCheckboxes)
        val preferredHotels = selectedCheckboxes(binding.hotelCheckboxes)

        // Validate selected boxes - At least one must be selected
        val airlineCheckboxesValid = setupViewModel.checkBoxValidation(preferredAirlines)
        val hotelCheckboxesValid = setupViewModel.checkBoxValidation(preferredHotels)

        if (airlineCheckboxesValid && hotelCheckboxesValid) {
            // Create setup request
            val setupAccountRequest = SetupAccountRequest(newPassword, preferredAirlines, preferredHotels)
            setupViewModel.setupAccount(setupAccountRequest)
        } else {
            Snackbar.make(binding.root, "Please select at least one airline and hotel", Snackbar.LENGTH_LONG).show()
        }

    }
}