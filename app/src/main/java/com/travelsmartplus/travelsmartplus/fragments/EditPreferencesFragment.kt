package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.databinding.FragmentEditPreferencesBinding
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown

/**
 * EditPreferencesFragment
 * Allows user to edit their preferences.
 *
 * @author Gabriel Salas
 */

class EditPreferencesFragment : Fragment() {

    private lateinit var binding: FragmentEditPreferencesBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model
    private val setupViewModel: SetupViewModel by activityViewModels() // Shared View Model
    private lateinit var user: User

    // Custom Dropdowns
    private val airlineOneDropdown = CustomDropdown()
    private val airlineTwoDropdown = CustomDropdown()
    private val hotelOneDropdown = CustomDropdown()
    private val hotelTwoDropdown = CustomDropdown()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners
        binding.editSaveBtn.setOnClickListener {
            savePreferences()
        }

        // Observers
        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            this.user = user

            binding.editCancelBtn.setOnClickListener {
                val action = EditPreferencesFragmentDirections.actionEditPreferencesFragmentToProfileFragment(user.id!!)
                findNavController().navigate(action)
            }
        }

        setupViewModel.airlines.observe(viewLifecycleOwner) { airlines ->
            airlineOneDropdown.setAirlineAutoComplete(requireContext(), binding.editPreferencesFirstAirline, airlines)
            airlineTwoDropdown.setAirlineAutoComplete(requireContext(), binding.editPreferencesSecondAirline, airlines)
        }

        setupViewModel.hotels.observe(viewLifecycleOwner) { hotels ->
            hotelOneDropdown.setHotelAutoComplete(requireContext(), binding.editPreferencesFirstHotel, hotels)
            hotelTwoDropdown.setHotelAutoComplete(requireContext(), binding.editPreferencesSecondHotel, hotels)
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }

    }

    private fun savePreferences() {

        // Get selected airlines
        val airlineOne = airlineOneDropdown.getSelectedAirline()
        val airlineTwo = airlineTwoDropdown.getSelectedAirline()
        val hotelOne = hotelOneDropdown.getSelectedHotel()
        val hotelTwo = hotelTwoDropdown.getSelectedHotel()
        val updatedUser = user

        // Validate selections
        val airlineSelectionValidation = (airlineOne != null) && (airlineTwo != null)
        val hotelSelectionValidation = (hotelOne != null) && (hotelTwo != null)

        if (airlineSelectionValidation && hotelSelectionValidation) {

            // Save updated user
            updatedUser.preferredAirlines = listOf(airlineOne!!.airlineName, airlineTwo!!.airlineName)
            updatedUser.preferredHotelChains = listOf(hotelOne!!.hotelChain, hotelTwo!!.hotelChain)

            userViewModel.updateUser(updatedUser.id.toString(), updatedUser)
            userViewModel.addEditSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Changes Saved!", Toast.LENGTH_SHORT).show()
                }
            }
            val action = EditPreferencesFragmentDirections.actionEditPreferencesFragmentToProfileFragment(updatedUser.id!!)
            findNavController().navigate(action)

        } else {
            Snackbar.make(binding.root, "Fields can't be empty." , Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNavigationView).show()
        }

    }


}