package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.databinding.FragmentSetupPreferencesBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown
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
    private val setupViewModel: SetupViewModel by activityViewModels() // Shared View Model

    // Custom Dropdowns
    private val airlineOneDropdown = CustomDropdown()
    private val airlineTwoDropdown = CustomDropdown()
    private val hotelOneDropdown = CustomDropdown()
    private val hotelTwoDropdown = CustomDropdown()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSetupPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.preferencesSetupBtn.setOnClickListener {
            setupAccount()
        }

        // Observer - observes responses and errors from View Model
        setupViewModel.setupAccountResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {
                findNavController().navigate(R.id.action_setupPreferencesFragment_to_setupFinishFragment)
            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        setupViewModel.airlines.observe(viewLifecycleOwner) { airlines ->
            airlineOneDropdown.setAirlineAutoComplete(requireContext(), binding.firstAirline, airlines)
            airlineTwoDropdown.setAirlineAutoComplete(requireContext(), binding.secondAirline, airlines)
        }

        setupViewModel.hotels.observe(viewLifecycleOwner) { hotels ->
            hotelOneDropdown.setHotelAutoComplete(requireContext(), binding.firstHotel, hotels)
            hotelTwoDropdown.setHotelAutoComplete(requireContext(), binding.secondHotel, hotels)
        }

        setupViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if(error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                setupViewModel.clearError()
            }
        }
    }

    private fun setupAccount() {
        // Get selected airlines
        val airlineOne = airlineOneDropdown.getSelectedAirline()
        val airlineTwo = airlineTwoDropdown.getSelectedAirline()
        val hotelOne = hotelOneDropdown.getSelectedHotel()
        val hotelTwo = hotelTwoDropdown.getSelectedHotel()

        // Validate selections
        val airlineSelectionValidation = (airlineOne != null) && (airlineTwo != null)
        val hotelSelectionValidation = (hotelOne != null) && (hotelTwo != null)

        if (airlineSelectionValidation && hotelSelectionValidation) {

            // Create setup request
            val password = setupViewModel.getPassword()
            val preferredAirlines = setOf(airlineOne!!.airlineName, airlineTwo!!.airlineName)
            val preferredHotels = setOf(hotelOne!!.hotelChain, hotelTwo!!.hotelChain)

            val setupAccountRequest = SetupAccountRequest(password!!, preferredAirlines, preferredHotels)

            setupViewModel.setupAccount(setupAccountRequest)

        } else {
            Snackbar.make(binding.root, "Fields can't be empty", Snackbar.LENGTH_LONG).show()
        }

    }


}