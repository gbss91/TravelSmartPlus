package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingSearchBinding
import com.travelsmartplus.travelsmartplus.databinding.FragmentNoPredictedBookingBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel

/**
 * NoPredictedBookingFragment
 * Shows when there is not enough data to make a prediction. Allows user to start a manual booking.
 *
 * @author Gabriel Salas
 */

class NoPredictedBookingFragment : Fragment() {

    private lateinit var binding: FragmentNoPredictedBookingBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoPredictedBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.manualBookingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_noPredictedBookingFragment_to_outboundFlightsFragment)
        }
    }

}