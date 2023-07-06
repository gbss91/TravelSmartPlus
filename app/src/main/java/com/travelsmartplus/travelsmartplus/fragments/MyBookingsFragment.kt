package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.BookingsAdapter
import com.travelsmartplus.travelsmartplus.databinding.FragmentMyBookingsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * MyBookingsFragment.
 * Displays current user's bookings.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class MyBookingsFragment : Fragment() {

    private lateinit var binding: FragmentMyBookingsBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get bookings for current user
        bookingViewModel.getUserBookings()

        val recyclerView = binding.myBookingsListView
        var adapter = BookingsAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observers
        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.myBookingsListView.visibility = View.INVISIBLE
                binding.myBookingsProgress.visibility = View.VISIBLE
            } else {
                binding.myBookingsListView.visibility = View.VISIBLE
                binding.myBookingsProgress.visibility = View.INVISIBLE
            }
        }

        bookingViewModel.myBookings.observe(viewLifecycleOwner) { myBookings ->
            if (myBookings.isNotEmpty()) {
                adapter = BookingsAdapter(myBookings)
                recyclerView.adapter = adapter
            } else {
                recyclerView.adapter = null
            }
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }

    }

    // Get user bookings data again when the fragment is resumed
    override fun onResume() {
        super.onResume()
        bookingViewModel.getUserBookings()
    }


}