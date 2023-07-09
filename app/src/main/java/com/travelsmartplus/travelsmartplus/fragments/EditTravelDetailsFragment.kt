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
import com.travelsmartplus.travelsmartplus.data.models.TravelData
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.databinding.FragmentEditTravelDetailsBinding
import com.travelsmartplus.travelsmartplus.utils.Formatters
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDatePicker
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * EditTravelDetailsFragment
 * Allows user to edit their travel details. These details are used to book trips.
 *
 * @author Gabriel Salas
 */

class EditTravelDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEditTravelDetailsBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model
    private lateinit var user: User

    // Countries Dropdown
    private val countriesDropdown = CustomDropdown()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditTravelDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customDatePicker = CustomDatePicker(childFragmentManager)
        userViewModel.getCountries()

        // Set click listeners
        binding.editDobInput.setOnClickListener {
            customDatePicker.showPastDatePicker(binding.editDobInput, "Date of Birth")
        }

        binding.editExpiryDateInput.setOnClickListener {
            customDatePicker.showDatePicker(binding.editExpiryDateInput, "Passport Expiry Date")
        }

        binding.editSaveBtn.setOnClickListener {
            saveTravelDetails()
        }

        // Observers
        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            this.user = user

            // Update UI with existing user data
            val userTravelData = user.travelData
            if (userTravelData != null ) {
                binding.editDobInput.setText(Formatters.formattedDateShort(userTravelData.dob))
                binding.editPassportNumberInput.setText(userTravelData.passportNumber)
                binding.editExpiryDateInput.setText(Formatters.formattedDateShort(userTravelData.passportExpiryDate))
            }

            binding.editCancelBtn.setOnClickListener {
                val action = EditTravelDetailsFragmentDirections.actionEditTravelDetailsFragmentToProfileFragment(user.id!!)
                findNavController().navigate(action)
            }
        }

        userViewModel.countries.observe(viewLifecycleOwner) { countries ->
            countriesDropdown.setCountriesAutoComplete(requireContext(), binding.editNationality, countries)
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }

    }

    // Get countries data on resume
    override fun onResume() {
        super.onResume()
        userViewModel.getCountries()
    }

    private fun saveTravelDetails() {

        val updatedUser = user

        // Get input
        val dob = binding.editDobInput
        val nationality = countriesDropdown.getSelectedCountry()
        val passportNumber = binding.editPassportNumberInput
        val expiryDate = binding.editExpiryDateInput

        // Input validation
        val inputValidation = userViewModel.editTravelDetailsValidation(dob, passportNumber, expiryDate)
        val nationalityValidation = nationality != null

        if (inputValidation && nationalityValidation) {

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val travelData = TravelData(
                userId = user.id!!,
                dob = LocalDate.parse(dob.text.toString(), formatter),
                nationality = nationality!!,
                passportNumber = passportNumber.text.toString(),
                passportExpiryDate = LocalDate.parse(expiryDate.text.toString(), formatter)
            )

            // Update user
            updatedUser.travelData = travelData
            userViewModel.updateUser(updatedUser.id.toString(), updatedUser)
            userViewModel.addEditSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Changes Saved!", Toast.LENGTH_SHORT).show()
                }
            }
            val action = EditTravelDetailsFragmentDirections.actionEditTravelDetailsFragmentToProfileFragment(updatedUser.id!!)
            findNavController().navigate(action)

        } else {
            Snackbar.make(binding.root, "Fields can't be empty." , Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNavigationView).show()
        }

    }

}