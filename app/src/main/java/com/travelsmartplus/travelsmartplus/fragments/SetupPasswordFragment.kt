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
import com.travelsmartplus.travelsmartplus.databinding.FragmentSetupPasswordBinding
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupPasswordFragment
 * Represents the password setup activity. Lets a new user to update their password.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupPasswordFragment : Fragment() {

    private lateinit var binding: FragmentSetupPasswordBinding
    private val setupViewModel: SetupViewModel by activityViewModels() // Shared View Model

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSetupPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupPassBtn.setOnClickListener {
            updatePassword()
        }

        // Observer - observes errors from View Model
        setupViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if(error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                setupViewModel.clearError()
            }
        }
    }

    private fun updatePassword() {

        val password = binding.setupPasswordInput
        val confirmPass = binding.setupConfirmPasswordInput

        // Input validation
        val inputValidation = setupViewModel.passwordValidation(password, confirmPass)

        // If valid go to next fragment and pass newPass arg
        if (inputValidation) {
            setupViewModel.setPassword(password.toString())
            findNavController().navigate(R.id.action_setupPasswordFragment_to_setupPreferencesFragment)
        }
    }

}