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
import com.travelsmartplus.travelsmartplus.data.models.requests.AddUserRequest
import com.travelsmartplus.travelsmartplus.databinding.FragmentAddUserBinding
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown
import dagger.hilt.android.AndroidEntryPoint

/**
 * AddUserFragment.
 * Allows administrator to add a new user
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model

    private val roleDropdown = CustomDropdown()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(inflater, container, false)

        // Set Role Dropdown
        val roleItems = resources.getStringArray(R.array.role_dropdown)
        roleDropdown.setSimpleDropdown(requireContext(), binding.addRoleInput, roleItems)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClick listeners
        binding.addSaveBtn.setOnClickListener {
            addUser()
        }

        binding.addCancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addUserFragment_to_usersFragment)
        }

        // Observers
        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }
    }

    private fun addUser() {

        val firstName = binding.addFirstNameInput
        val lastName = binding.addLastNameInput
        val email = binding.addEmailInput
        val role = roleDropdown.getValueForSelectedItem(resources.getStringArray(R.array.role_dropdown_values))
        val password = binding.addPassInput
        val confirmPass = binding.addConfirmPassInput

        // Input Validation
        val inputValidation = userViewModel.editedDetailsValidation(firstName, lastName, email)
        val passValidation = userViewModel.passwordValidation(password, confirmPass)

        if (inputValidation && passValidation) {

            val newUser = AddUserRequest(
                firstName = firstName.text.toString(),
                lastName = lastName.text.toString(),
                email = email.text.toString(),
                admin = role.toBoolean(),
                password = password.text.toString()
            )

            // Add user
            userViewModel.addUser(newUser)
            findNavController().navigate(R.id.action_addUserFragment_to_usersFragment)

        }
    }
}