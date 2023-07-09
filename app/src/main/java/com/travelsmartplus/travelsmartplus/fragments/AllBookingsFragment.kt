package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentAllBookingsBinding
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingConfirmationBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * AllBookingsFragment.
 * Displays all company bookings to the administrator.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class AllBookingsFragment : Fragment() {

    private lateinit var binding: FragmentAllBookingsBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Mode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.allBookingsFragmentProgress)



    }

}