package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.databinding.FragmentSetupPreferencesBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupPreferencesFragment
 * Represents the preferences setup activity. Lets a new user to add preferences to their account.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupPreferencesFragment : Fragment() {

    private lateinit var binding: FragmentSetupPreferencesBinding
    private val setupViewModel: SetupViewModel by viewModels()
    private val args: SetupPreferencesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetupPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get password from previous SetupPasswordFragment
        val newPass = args.newPass

        binding.preferencesSetupBtn.setOnClickListener {
            setupAccount(newPass)
        }

        // Observer - observes responses and errors from View Model
        setupViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }

        setupViewModel.setupAccountResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {
                findNavController().navigate(R.id.action_setupPreferencesFragment_to_setupFinishFragment)
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