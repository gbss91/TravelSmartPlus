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
import com.travelsmartplus.travelsmartplus.adapters.HotelResultsAdapter
import com.travelsmartplus.travelsmartplus.adapters.OnItemClickListener
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.HotelBooking
import com.travelsmartplus.travelsmartplus.databinding.FragmentHotelsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * HotelsFragment
 * Displays hotel offers results. Can be used for manual booking and to modify a predicted booking.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class HotelsFragment : Fragment(), OnItemClickListener<HotelBooking> {

    private lateinit var binding: FragmentHotelsBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model
    private lateinit var updatedBooking: Booking

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

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.hotelResultsProgress)

        val recyclerView = binding.hotelResultsView
        var adapter = HotelResultsAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Observers
        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.hotelResultsView.visibility = View.INVISIBLE
                binding.hotelResultsProgress.visibility = View.VISIBLE
            } else {
                binding.hotelResultsView.visibility = View.VISIBLE
                binding.hotelResultsProgress.visibility = View.INVISIBLE
            }
        }

        bookingViewModel.hotelOffers.observe(viewLifecycleOwner) { hotelOffers ->
            if (hotelOffers.isNotEmpty()) {
                adapter = HotelResultsAdapter(hotelOffers, this)
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
        bookingViewModel.getHotelOffers()
    }

    override fun onItemClick(item: HotelBooking) {

        // Update hotel for predicted booking
        val newTotal = updatedBooking.totalPrice - updatedBooking.hotelBooking!!.totalPrice
        updatedBooking.hotelBooking = item
        updatedBooking.totalPrice = newTotal + item.totalPrice
        bookingViewModel.setBooking(updatedBooking)

        findNavController().navigate(R.id.action_hotelsFragment_to_predictedBookingFragment)
    }
}