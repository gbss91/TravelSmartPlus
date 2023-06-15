package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentInboundFlightsBinding
import com.travelsmartplus.travelsmartplus.databinding.FragmentOutboundFlightsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel

/**
 * InboundFlightsFragment
 * Displays inbound flights results. Can be used for manual booking and to modify a predicted booking.
 *
 * @author Gabriel Salas
 */

class InboundFlightsFragment : Fragment() {

    private lateinit var binding: FragmentInboundFlightsBinding
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
        binding = FragmentInboundFlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}