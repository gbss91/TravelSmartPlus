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
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.FlightResultsAdapter
import com.travelsmartplus.travelsmartplus.adapters.OnItemClickListener
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.databinding.FragmentFlightsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * FlightsFragment
 * Displays flights results. Can be used for manual booking or to modify a predicted booking.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class FlightsFragment : Fragment(), OnItemClickListener<FlightBooking> {

    private lateinit var binding: FragmentFlightsBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model
    private lateinit var updatedBooking: Booking


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.flightResultsView
        var adapter = FlightResultsAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.flightResultsProgress)

        // Observers
        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.flightResultsView.visibility = View.INVISIBLE
                binding.flightResultsProgress.visibility = View.VISIBLE
            } else {
                binding.flightResultsView.visibility = View.VISIBLE
                binding.flightResultsProgress.visibility = View.INVISIBLE
            }
        }

        bookingViewModel.flightOffers.observe(viewLifecycleOwner) { flightOffers ->
            if (flightOffers.isNotEmpty()) {
                adapter = FlightResultsAdapter(flightOffers, this)
                recyclerView.adapter = adapter
            } else {
                recyclerView.adapter = null

            }
        }

        bookingViewModel.booking.observe(viewLifecycleOwner) { booking ->
            this.updatedBooking = booking!!
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }
    }

    // Get flight data again when the fragment is resumed or it becomes visible
    override fun onResume() {
        super.onResume()
        bookingViewModel.getFlightOffers()
    }

    override fun onItemClick(item: FlightBooking) {

        // Update the flight data for predicted booking
        val newTotal = updatedBooking.totalPrice - updatedBooking.flightBooking.totalPrice
        updatedBooking.flightBooking = item
        updatedBooking.totalPrice = newTotal + item.totalPrice
        bookingViewModel.setBooking(updatedBooking)

        findNavController().navigate(R.id.action_flightsFragment_to_predictedBookingFragment)
    }
}