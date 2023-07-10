package com.travelsmartplus.travelsmartplus.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingBinding
import com.travelsmartplus.travelsmartplus.utils.ErrorMessages
import com.travelsmartplus.travelsmartplus.utils.Formatters
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.toJavaLocalDateTime

/**
 * BookingFragment
 * Displays a booking.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class BookingFragment : Fragment() {

    private lateinit var binding: FragmentBookingBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model
    private val args : BookingFragmentArgs by navArgs() // Fragment SafeArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.bookingProgress)

        // Get booking data
        val bookingId = args.bookingId
        bookingViewModel.getBooking(bookingId.toString())

        // Toggle Price Breakdown
        var priceBreakdown = false
        binding.bookingPriceBreakdownLine.visibility = View.GONE
        binding.bookingPriceBreakdownContainer.visibility = View.GONE

        // Set click listener for price
        binding.bookingPriceBtn.setOnClickListener {
            if (!priceBreakdown) {
                binding.bookingPriceBreakdownLine.visibility = View.VISIBLE
                binding.bookingPriceBreakdownContainer.visibility = View.VISIBLE
                binding.bookingPriceBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up))
                priceBreakdown = true
            } else {
                binding.bookingPriceBreakdownLine.visibility = View.GONE
                binding.bookingPriceBreakdownContainer.visibility = View.GONE
                binding.bookingPriceBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down))
                priceBreakdown = false
            }
        }

        // Set the delete button with the userID
        binding.cancelBookingBtn.setOnClickListener {
            deleteBooking(bookingId)
        }

        // Observers
        bookingViewModel.booking.observe(viewLifecycleOwner) { booking->
            if (booking != null) {

                binding.bookingTitle.text = booking.destination.city
                binding.bookingId.text = getString(R.string.booking_fragment_id, booking.id.toString())
                binding.bookingFlightReference.text = booking.flightBooking.bookingReference

                // Assign data to flights
                val outbound = booking.flightBooking.segments[0]
                binding.bookingOutboundFlight.flightItemAirline.text = outbound.flights[0].carrierName
                binding.bookingOutboundFlight.flightItemDepartureTimeText.text = Formatters.formattedTime(outbound.flights[0].departureTime.toJavaLocalDateTime())
                binding.bookingOutboundFlight.flightItemOriginIataText.text = outbound.flights[0].departureAirport.iataCode
                binding.bookingOutboundFlight.flightItemDuration.text = Formatters.formattedDuration(outbound.duration, requireContext())
                binding.bookingOutboundFlight.flightItemStops.text = Formatters.formattedStops(outbound)
                binding.bookingOutboundFlight.flightItemArrivalTimeText.text = Formatters.formattedArrivalTime(outbound.flights[0].departureTime.toJavaLocalDateTime(), outbound.flights.last().arrivalTime.toJavaLocalDateTime())
                binding.bookingOutboundFlight.flightItemDestinationIataText.text = outbound.flights.last().arrivalAirport.iataCode

                // Assign and show return only when needed
                if (!booking.flightBooking.oneWay) {
                    binding.bookingInboundFlight.root.visibility = View.VISIBLE

                    val inbound = booking.flightBooking.segments[1]
                    binding.bookingInboundFlight.flightItemAirline.text = inbound.flights[0].carrierName
                    binding.bookingInboundFlight.flightItemDepartureTimeText.text = Formatters.formattedTime(inbound.flights[0].departureTime.toJavaLocalDateTime())
                    binding.bookingInboundFlight.flightItemOriginIataText.text = inbound.flights[0].departureAirport.iataCode
                    binding.bookingInboundFlight.flightItemDuration.text = Formatters.formattedDuration(inbound.duration, requireContext())
                    binding.bookingInboundFlight.flightItemStops.text = Formatters.formattedStops(inbound)
                    binding.bookingInboundFlight.flightItemArrivalTimeText.text = Formatters.formattedArrivalTime(inbound.flights[0].departureTime.toJavaLocalDateTime(), inbound.flights.last().arrivalTime.toJavaLocalDateTime())
                    binding.bookingInboundFlight.flightItemDestinationIataText.text = inbound.flights.last().arrivalAirport.iataCode

                } else {
                    binding.bookingInboundFlight.root.visibility = View.GONE
                }

                // Assign data to hotel and show elements
                if (booking.hotelBooking != null ) {
                    val nights = booking.hotelBooking!!.checkOutDate.dayOfYear - booking.hotelBooking!!.checkInDate.dayOfYear
                    binding.bookingLine.visibility = View.VISIBLE
                    binding.bookingHotelContainer.visibility = View.VISIBLE
                    binding.bookingHotelPriceRow.visibility = View.VISIBLE
                    binding.bookingHotelName.text = booking.hotelBooking!!.hotelName
                    binding.bookingHotelAddress.text = booking.hotelBooking!!.address
                    binding.bookingHotelRoomType.text = booking.hotelBooking?.roomType ?: "" // Room type might be empty
                    binding.bookingHotelTotal.text = requireContext().getString(R.string.predicted_hotel_price, nights.toString(), booking.hotelBooking!!.rate, booking.hotelBooking!!.totalPrice.toInt().toString())

                } else {
                    binding.bookingLine.visibility = View.GONE
                    binding.bookingHotelContainer.visibility = View.GONE
                    binding.bookingHotelPriceRow.visibility = View.GONE
                }

                binding.bookingFlightTotal.text = requireContext().getString(R.string.currency_price, booking.flightBooking.totalPrice.toInt().toString())
                binding.bookingPriceText.text = requireContext().getString(R.string.currency_price, booking.totalPrice.toInt().toString())



            } else {
                binding.bookingGroup.visibility = View.GONE
            }
        }

        bookingViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.bookingGroup.visibility = View.GONE
                binding.bookingProgress.visibility = View.VISIBLE
            } else {
                binding.bookingGroup.visibility = View.VISIBLE
                binding.bookingProgress.visibility = View.GONE
            }
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }
    }

    // Delete booking
    private fun deleteBooking(bookingId: Int) {

        // Create Dialog
        val confirmationDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.booking_fragment_dialog_title))
            .setMessage(getString(R.string.booking_fragment_dialog_text))
            .setPositiveButton(getString(R.string.booking_fragment_dialog_delete)) { dialog, _ ->
                dialog.dismiss()
                confirmDeletion(bookingId)
            }
            .setNegativeButton(getString(R.string.booking_fragment_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        confirmationDialog.show()

    }

    private fun confirmDeletion(bookingId: Int) {

        // Make call to delete account
        bookingViewModel.deleteBooking(bookingId.toString())

        // Observe the response from userViewModel
        bookingViewModel.deleteBookingResponse.observe(viewLifecycleOwner) { response ->
            if (response != null && response.isSuccessful) {

                // Show Toast
                Toast.makeText(requireContext(), "Booking Cancelled", Toast.LENGTH_SHORT).show()

                // Navigate back to previous screen - Either All Bookings or My Bookings
                findNavController().popBackStack()


            } else {
                val error = response?.errorBody()?.string() ?: ErrorMessages.UNKNOWN_ERROR
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}