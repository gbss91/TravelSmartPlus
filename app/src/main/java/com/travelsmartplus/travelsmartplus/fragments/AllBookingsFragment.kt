package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.AllBookingsAdapter
import com.travelsmartplus.travelsmartplus.adapters.OnItemClickListener
import com.travelsmartplus.travelsmartplus.databinding.FragmentAllBookingsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * AllBookingsFragment.
 * Displays all company bookings to the administrator.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class AllBookingsFragment : Fragment(), OnItemClickListener<Int> {

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

        val recyclerView = binding.allBookingsListView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Observers
        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.usersTable.visibility = View.INVISIBLE
                binding.allBookingsFragmentProgress.visibility = View.VISIBLE
            } else {
                binding.usersTable.visibility = View.VISIBLE
                binding.allBookingsFragmentProgress.visibility = View.INVISIBLE
            }
        }

        bookingViewModel.allBookings.observe(viewLifecycleOwner) { allBookings->

            if (allBookings.isNotEmpty()) {
                val sortedBookings = allBookings.sortedBy { it.departureDate }
                val adapter = AllBookingsAdapter(sortedBookings, this)
                recyclerView.adapter = adapter
            } else {
                recyclerView.adapter = null
            }

            // Sort by destination
            binding.allBookingsDestinationHeader.setOnClickListener {
                val sortedBookings = allBookings.sortedBy { it.destination.city }
                recyclerView.adapter = AllBookingsAdapter(sortedBookings, this)
            }

            // Sort by departure date
            binding.allBookingsDateHeader.setOnClickListener {
                val sortedBookings = allBookings.sortedBy { it.departureDate }
                recyclerView.adapter = AllBookingsAdapter(sortedBookings, this)
            }
        }
    }

    // Get bookings data when the fragment is resumed or becomes visible
    override fun onResume() {
        super.onResume()
        bookingViewModel.getAllBookings()
    }

    // Handle clicking user row - Sends booking ID to open correct booking
    override fun onItemClick(item: Int) {
        val action = AllBookingsFragmentDirections.actionAllBookingsFragmentToBookingFragment(item)
        findNavController().navigate(action)
    }

}