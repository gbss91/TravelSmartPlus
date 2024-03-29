package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.BookingsAdapter
import com.travelsmartplus.travelsmartplus.adapters.OnItemClickListener
import com.travelsmartplus.travelsmartplus.databinding.FragmentMyBookingsBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

/**
 * MyBookingsFragment.
 * Displays current user's bookings.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class MyBookingsFragment : Fragment(), OnItemClickListener<Int> {

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

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.myBookingsProgress)

        val recyclerView = binding.myBookingsListView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val bookingsAdapter = BookingsAdapter(emptyList(), this)
        recyclerView.adapter = bookingsAdapter


        // Set search bar
        binding.myBookingsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                bookingsAdapter.filter.filter(newText)
                return true
            }
        })

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
            val sortedBookings = myBookings.sortedBy { it.departureDate }.filter { it.departureDate > LocalDate.now().toKotlinLocalDate() }
            if (myBookings.isNotEmpty()) {
                bookingsAdapter.updateBookings(sortedBookings)
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

    override fun onItemClick(item: Int) {
        val action = MyBookingsFragmentDirections.actionMyBookingsFragmentToBookingFragment(item)
        findNavController().navigate(action)
    }


}