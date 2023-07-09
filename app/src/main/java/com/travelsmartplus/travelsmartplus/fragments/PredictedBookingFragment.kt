package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentPredictedBookingBinding
import com.travelsmartplus.travelsmartplus.databinding.ItemFlightTemplateBinding
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedArrivalTime
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedDuration
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedStops
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedTime
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * PredictedBookingFragment.
 * Displays the suggested booking to the user.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class PredictedBookingFragment : Fragment() {

    private lateinit var binding: FragmentPredictedBookingBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPredictedBookingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.predictedBookingProgress)

        // Set title to destination
        binding.predictedBookingTitle.text = bookingViewModel.getBookingSearchRequest()!!.destination.city

        // Toggle Price Breakdown
        var priceBreakdown = false
        binding.predictedBookingPriceBreakdownLine.visibility = GONE
        binding.predictedBookingPriceBreakdownContainer.visibility = GONE

        // Set button listeners
        binding.flightEditBtn.setOnClickListener {
            findNavController().navigate(R.id.action_predictedBookingFragment_to_flightsFragment)
        }

        binding.hotelEditBtn.setOnClickListener {
            findNavController().navigate(R.id.action_predictedBookingFragment_to_hotelsFragment)
        }

        binding.predictedBookingPriceBtn.setOnClickListener {
            if (!priceBreakdown) {
                binding.predictedBookingPriceBreakdownLine.visibility = VISIBLE
                binding.predictedBookingPriceBreakdownContainer.visibility = VISIBLE
                binding.predictedBookingPriceBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up))
                priceBreakdown = true
            } else {
                binding.predictedBookingPriceBreakdownLine.visibility = GONE
                binding.predictedBookingPriceBreakdownContainer.visibility = GONE
                binding.predictedBookingPriceBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down))
                priceBreakdown = false
            }
        }

        binding.predictedBookingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_predictedBookingFragment_to_bookingConfirmationFragment)
        }

        // Observers

        // Only search when new search. Avoids searching again when only making changes.
        bookingViewModel.newSearch.observe(viewLifecycleOwner) { newSearch ->
            if(newSearch) {
                bookingViewModel.bookingSearch()
                bookingViewModel.setNewSearch(false) // Avoids searching again if back from flight/hotel selection
            }
        }

        bookingViewModel.booking.observe(viewLifecycleOwner) { booking ->
            if (booking != null) {

                // Assign data to flights
                val outbound = booking.flightBooking.segments[0]
                binding.predictedOutboundFlight.flightItemAirline.text = outbound.flights[0].carrierName
                binding.predictedOutboundFlight.flightItemDepartureTimeText.text = formattedTime(outbound.flights[0].departureTime)
                binding.predictedOutboundFlight.flightItemOriginIataText.text = outbound.flights[0].departureAirport.iataCode
                binding.predictedOutboundFlight.flightItemDuration.text = formattedDuration(outbound.duration, requireContext())
                binding.predictedOutboundFlight.flightItemStops.text = formattedStops(outbound)
                binding.predictedOutboundFlight.flightItemArrivalTimeText.text = formattedArrivalTime(outbound.flights[0].departureTime, outbound.flights.last().arrivalTime)
                binding.predictedOutboundFlight.flightItemDestinationIataText.text = outbound.flights.last().arrivalAirport.iataCode

                // Assign and show return only when needed
                if (!booking.flightBooking.oneWay) {
                    binding.predictedInboundFlight.root.visibility = VISIBLE

                    val inbound = booking.flightBooking.segments[1]
                    binding.predictedInboundFlight.flightItemAirline.text = inbound.flights[0].carrierName
                    binding.predictedInboundFlight.flightItemDepartureTimeText.text = formattedTime(inbound.flights[0].departureTime)
                    binding.predictedInboundFlight.flightItemOriginIataText.text = inbound.flights[0].departureAirport.iataCode
                    binding.predictedInboundFlight.flightItemDuration.text = formattedDuration(inbound.duration, requireContext())
                    binding.predictedInboundFlight.flightItemStops.text = formattedStops(inbound)
                    binding.predictedInboundFlight.flightItemArrivalTimeText.text = formattedArrivalTime(inbound.flights[0].departureTime, inbound.flights.last().arrivalTime)
                    binding.predictedInboundFlight.flightItemDestinationIataText.text = inbound.flights.last().arrivalAirport.iataCode

                } else {
                    binding.predictedInboundFlight.root.visibility = GONE
                }

                // Assign data to hotel and show elements
                if (booking.hotelBooking != null ) {
                    val nights = booking.hotelBooking!!.checkOutDate.dayOfYear - booking.hotelBooking!!.checkInDate.dayOfYear
                    binding.predictedBookingLine.visibility = VISIBLE
                    binding.predictedBookingHotelContainer.visibility = VISIBLE
                    binding.predictedHotelPriceRow.visibility = VISIBLE
                    binding.predictedHotelName.text = booking.hotelBooking!!.hotelName
                    binding.predictedHotelAddress.text = booking.hotelBooking!!.address
                    binding.predictedHotelRoomType.text = booking.hotelBooking?.roomType ?: "" // Room type might be empty
                    binding.predictedHotelTotal.text = requireContext().getString(R.string.predicted_hotel_price, nights.toString(), booking.hotelBooking!!.rate, booking.hotelBooking!!.totalPrice.toInt().toString())

                } else {
                    binding.predictedBookingLine.visibility = GONE
                    binding.predictedBookingHotelContainer.visibility = GONE
                    binding.predictedHotelPriceRow.visibility = GONE
                }

                binding.predictedFlightTotal.text = requireContext().getString(R.string.currency_price, booking.flightBooking.totalPrice.toInt().toString())
                binding.predictedBookingPriceText.text = requireContext().getString(R.string.currency_price, booking.totalPrice.toInt().toString())

            } else {
                binding.predictedBookingGroup.visibility = GONE
                binding.predictedBookingNoResults.visibility = VISIBLE
            }

        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.predictedBookingNoResults.visibility = GONE
                binding.predictedBookingGroup.visibility = GONE
                binding.predictedBookingProgress.visibility = VISIBLE
            } else {
                binding.predictedBookingGroup.visibility = VISIBLE
                binding.predictedBookingProgress.visibility = GONE
            }
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }

    }

}
