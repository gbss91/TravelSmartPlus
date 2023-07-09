package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingConfirmationBinding
import com.travelsmartplus.travelsmartplus.databinding.FragmentPredictedBookingBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * BookingConfirmationFragment.
 * Displays the booking confirmation with relevant booking details.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class BookingConfirmationFragment : Fragment() {

    private lateinit var binding: FragmentBookingConfirmationBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Mode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookingConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.bookingConfirmationProgress)

        // Add booking
        bookingViewModel.addBooking()

        // Observers
        bookingViewModel.booking.observe(viewLifecycleOwner) { booking ->
            binding.bookingConfirmationText.text = getString(R.string.confirmation_fragment_id, (booking?.id ?: ""))
        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.bookingConfirmationContainer.visibility = GONE
                binding.bookingConfirmationProgress.visibility = VISIBLE
            } else {
                binding.bookingConfirmationContainer.visibility = VISIBLE
                binding.bookingConfirmationProgress.visibility = GONE
            }
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                findNavController().navigate(R.id.action_bookingConfirmationFragment_to_bookingSearchFragment)
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }

        // Set GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.plane_window)
            .into(binding.bookingConfirmationIcon)

    }



}