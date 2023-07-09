package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.UpdatePasswordRequest
import com.travelsmartplus.travelsmartplus.databinding.FragmentEditDetailsBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown

/**
 * EditDetailsFragment
 * Allows user to edit basic details.
 *
 * @author Gabriel Salas
 */

class EditDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEditDetailsBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model

    private var passwordChanged = false
    private var isAdmin = false
    private lateinit var user: User
    private val roleDropdown = CustomDropdown()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAdmin = userViewModel.isAdmin()

        // Hide confirm password field and only show if user changes password field
        binding.editConfirmPassContainer.visibility = GONE
        binding.editPassInput.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.editConfirmPassContainer.visibility = VISIBLE
                binding.editPassInput.setText("")
                passwordChanged = true
            }
        }

        // Set click listeners
        binding.editSaveBtn.setOnClickListener {
            saveBasicDetails()
        }

        // Observers
        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            this.user = user

            // Set Role Dropdown with user role
            val roleItems = resources.getStringArray(R.array.role_dropdown)
            roleDropdown.setSimpleDropdown(requireContext(), binding.editRoleInput, roleItems)

            val userRoleIndex = if (user.admin) 1 else 0
            binding.editRoleInput.setText(roleItems[userRoleIndex], false)
            roleDropdown.setSelectedItem(userRoleIndex)

            // Update UI for admins - Allows to reuse edit fragment
            val currentUser = userViewModel.getCurrentUser()
            if (isAdmin && user.id != currentUser) {
                binding.editPassContainer.visibility = GONE
            } else {
                binding.editRoleContainer.visibility = GONE
            }

            // Update fields with existing user data
            binding.editFirstNameInput.setText(user.firstName)
            binding.editLastNameInput.setText(user.lastName)
            binding.editEmailInput.setText(user.email)
            binding.editPassInput.setText("********") // Placeholder - Only change password if user clicks input field

            binding.editCancelBtn.setOnClickListener {
                val action = EditDetailsFragmentDirections.actionEditDetailsFragmentToProfileFragment(user.id!!)
                findNavController().navigate(action)
            }

        }

        userViewModel.passwordResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                val action = EditDetailsFragmentDirections.actionEditDetailsFragmentToProfileFragment(user.id!!)
                findNavController().navigate(action)
            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }
    }

    private fun saveBasicDetails() {

        val firstName = binding.editFirstNameInput
        val lastName = binding.editLastNameInput
        val email = binding.editEmailInput

        val updatedUser = user

        // Input validation
        val inputValidation = userViewModel.editedDetailsValidation(firstName, lastName, email)

        if (inputValidation) {

            updatedUser.firstName = firstName.text.toString()
            updatedUser.lastName = lastName.text.toString()
            updatedUser.email = email.text.toString()

            if(isAdmin) {
                val role = roleDropdown.getValueForSelectedItem(resources.getStringArray(R.array.role_dropdown_values))
                updatedUser.admin = role.toBoolean()
            }

            // Check if password change and perform extra validation
            if (passwordChanged) {
                val password = binding.editPassInput
                val confirmPass = binding.editConfirmPassInput

                // Password validation
                val passValidation = userViewModel.passwordValidation(password, confirmPass)

                if (passValidation) {

                    // Update user and password - Observer caches respond and continues accordingly
                    userViewModel.updateUser(updatedUser.id.toString(), updatedUser)
                    userViewModel.passwordUpdate(UpdatePasswordRequest(updatedUser.id!!, password.text.toString()))

                }
            } else {
                // Update user without password
                userViewModel.updateUser(updatedUser.id.toString(), updatedUser)
                val action = EditDetailsFragmentDirections.actionEditDetailsFragmentToProfileFragment(updatedUser.id!!)
                findNavController().navigate(action)
            }
        } else {
            Snackbar.make(binding.root, "Fields can't be empty." , Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNavigationView).show()
        }
    }


}