package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentHotelsBinding
import com.travelsmartplus.travelsmartplus.databinding.FragmentInboundFlightsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel

/**
 * HotelsFragment
 * Displays hotel offers results. Can be used for manual booking and to modify a predicted booking.
 *
 * @author Gabriel Salas
 */

class HotelsFragment : Fragment() {

    private lateinit var binding: FragmentHotelsBinding
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
        binding = FragmentHotelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}