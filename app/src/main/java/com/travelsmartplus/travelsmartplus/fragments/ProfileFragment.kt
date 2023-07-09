package com.travelsmartplus.travelsmartplus.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.activities.LandingPageActivity
import com.travelsmartplus.travelsmartplus.databinding.FragmentProfileBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedDateShort
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.MainViewModel
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

/**
 * ProfileFragment
 * Displays current's user profile. Allows user to edit and delete their own details.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model
    private val mainViewModel: MainViewModel by activityViewModels() // Shared View Model - This handles session
    private val args : ProfileFragmentArgs by navArgs() // Fragment SafeArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get user data
        val userId = arguments?.getInt("userId") ?: args.userId
        userViewModel.getUser(userId.toString())

        // Set click listeners
        binding.signOutBtn.setOnClickListener {
            signOut()
        }

        // Set Edit buttons
        binding.profileFragmentDetailsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editDetailsFragment)
        }

        binding.profileFragmentTravelDetailsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editTravelDetailsFragment)
        }

        binding.profileFragmentPreferencesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editPreferencesFragment)
        }

        // Observers
        userViewModel.userData.observe(viewLifecycleOwner) { user ->

            // Only shows relevant sections - Allows to reuse profile
            val currentUser = userViewModel.getCurrentUser()
            if (userId != currentUser) {
                binding.profileFragmentTravelDetailsCard.visibility = GONE
                binding.profileFragmentPreferencesCard.visibility = GONE
                binding.profileDetailsPass.visibility = GONE
                binding.signOutBtn.visibility = GONE
            }

            // Update UI with user data
            binding.profileFragmentTitle.text = context?.getString(R.string.profile_fragment_title, user.firstName, user.lastName)
            binding.profileDetailsName.text = context?.getString(R.string.profile_fragment_name, user.firstName, user.lastName)
            binding.profileDetailsEmail.text = user.email
            binding.profileDetailsRole.text = if (user.admin) "Administrator" else "Staff"

            // Update travel data if available
            val travelData = user.travelData
            if (travelData != null) {
                binding.profileTravelDetailsDob.text = formattedDateShort(user.travelData!!.dob)
                binding.profileTravelDetailsInfo.text = getString(R.string.profile_fragment_masked_data)
            } else {
                binding.profileTravelDetailsInfo.text = "N/A"
                binding.profileTravelDetailsDob.text = "N/A"
            }

            // Update preferred Airlines if available
            val preferredAirlines = user.preferredAirlines
            if (preferredAirlines != null) {
                binding.profilePreferencesAirlines.text = preferredAirlines.joinToString()
            } else {
                binding.profilePreferencesAirlines.text = "N/A"
            }

            // Update preferred Hotels if available
            val preferredHotels = user.preferredHotelChains
            if (preferredHotels != null) {
                binding.profilePreferencesHotels.text = preferredHotels.joinToString()
            } else {
                binding.profilePreferencesHotels.text = "N/A"
            }

            // Set the delete button with the userID
            binding.deleteAccountLink.setOnClickListener {
                deleteAccount(user.id.toString())
            }

        }

        userViewModel.addEditSuccessful.observe(viewLifecycleOwner) { successful ->
            if (successful) {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                findNavController().navigate(R.id.action_predictedBookingFragment_to_bookingSearchFragment)
                userViewModel.clearError()
            }
        }
    }

    // Sign out user
    private fun signOut() {
        userViewModel.signOut()

        // Navigate to Landing Page and Finish Main Activity - Avoids returning when pressing back button
        val intent = Intent(requireContext(), LandingPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()

    }

    // Delete Account
    private fun deleteAccount(userId: String) {

        // Create Dialog
        val confirmationDialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Account Deletion")
            .setMessage("Account deletion is permanent.")
            .setPositiveButton("Delete") { dialog, _ ->
                dialog.dismiss()
                confirmDeletion(userId)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        confirmationDialog.show()
    }

    private fun confirmDeletion(userId: String) {

        // Make call to delete account
        userViewModel.deleteUser(userId)

        // Observe the response from userViewModel
        userViewModel.deleteUserResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {

                // Show Toast
                Toast.makeText(requireContext(), "Account Deleted", Toast.LENGTH_SHORT).show()

                // Navigate to landing page is current user is deleted, else to user - Allows to reuse profile fragment
                val currentUser = userViewModel.getCurrentUser()
                if (userId.toInt() == currentUser) {

                    mainViewModel.clearSession()

                    val intent = Intent(requireContext(), LandingPageActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish() // Avoid returning when pressing back button
                } else {
                    findNavController().navigate(R.id.action_profileFragment_to_usersFragment)
                }

            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

    }

}