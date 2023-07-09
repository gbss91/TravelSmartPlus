package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingSearchBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import com.travelsmartplus.travelsmartplus.widgets.CustomDatePicker
import com.travelsmartplus.travelsmartplus.widgets.CustomDropdown
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * BookingSearchFragment
 * Fragment for searching flights and hotels.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class BookingSearchFragment : Fragment() {

    private lateinit var binding: FragmentBookingSearchBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() // Shared View Model

    // Custom Dropdowns
    private val onewayDropdown = CustomDropdown()
    private val adultsDropdown = CustomDropdown()
    private val bookingClassesDropdown = CustomDropdown()
    private val fromAutocomplete = CustomDropdown()
    private val toAutocomplete = CustomDropdown()

    // Store booking options and selected airports
    private var flightHotel = false
    private var oneWay = false
    private var originCity: Airport? = null
    private var destinationCity: Airport? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookingSearchBinding.inflate(inflater, container, false)

        // Set flight only button checked and remove hotel fields as default state
        binding.searchToggleGroup.check(R.id.flightOnlySearchBtn)
        binding.checkInDateContainer.visibility = View.GONE
        binding.checkOutDateContainer.visibility = View.GONE

        // Set simple Dropdowns
        val adultsDropdownItems = resources.getStringArray(R.array.adults_dropdown)
        val bookingClassesDropdownItems = resources.getStringArray(R.array.booking_classes_dropdown)
        adultsDropdown.setSimpleDropdown(requireContext(), binding.adultsDropdownInput, adultsDropdownItems)
        bookingClassesDropdown.setSimpleDropdown(requireContext(), binding.bookingClassDropdownInput, bookingClassesDropdownItems)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customDatePicker = CustomDatePicker(childFragmentManager)

        // Toggle button functionality
        binding.searchToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.flightOnlySearchBtn -> {
                        flightHotel = false
                        binding.checkInDateContainer.visibility = View.GONE
                        binding.checkOutDateContainer.visibility = View.GONE
                    }
                    R.id.flightHotelSearchBtn -> {
                        flightHotel = true
                        binding.checkInDateContainer.visibility = View.VISIBLE
                        binding.checkOutDateContainer.visibility = View.VISIBLE
                    }
                }
            }
        }

        // Set One-way dropdown - uses callback to update UI
        val onewayDropdownItems = resources.getStringArray(R.array.oneway_dropdown)
        onewayDropdown.setActionDropdown(requireContext(), binding.onewayDropdownInput, onewayDropdownItems) { selectedItem ->
            when (selectedItem) {
                "ONE-WAY" -> {
                    oneWay = true
                    binding.returnDateContainer.visibility = View.GONE
                }
                "RETURN" -> {
                    oneWay = false
                    binding.returnDateContainer.visibility = View.VISIBLE
                }
            }
        }

        binding.departureDateSearchInput.setOnClickListener {
            if (oneWay) {
                customDatePicker.showDatePicker(binding.departureDateSearchInput, "Flight Date")
            } else {
                customDatePicker.showDateRangePicker(binding.departureDateSearchInput, binding.returnDateSearchInput,"Flight Dates")
            }
        }

        binding.returnDateSearchInput.setOnClickListener {
            customDatePicker.showDateRangePicker(binding.departureDateSearchInput, binding.returnDateSearchInput, "Flight Dates")
        }

        binding.checkInDateInput.setOnClickListener {
            customDatePicker.showDateRangePicker(binding.checkInDateInput, binding.checkOutDateInput,"Hotel Dates")
        }

        binding.checkOutDateInput.setOnClickListener {
            customDatePicker.showDateRangePicker(binding.checkOutDateInput, binding.checkOutDateInput,"Hotel Dates")
        }

        binding.searchBtn.setOnClickListener {
            bookingSearch()
        }

        // Observers
        bookingViewModel.airports.observe(viewLifecycleOwner) { airports ->
            fromAutocomplete.setAirportAutocomplete(requireContext(), binding.fromSearchInput, airports)
            toAutocomplete.setAirportAutocomplete(requireContext(), binding.toSearchInput, airports)
        }

        bookingViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                bookingViewModel.clearError()
            }
        }

    }

    // Get airport data on resume
    override fun onResume() {
        super.onResume()
        bookingViewModel.airportsAutoComplete()
    }

    private fun bookingSearch() {

        originCity = fromAutocomplete.getSelectedAirport()
        destinationCity = toAutocomplete.getSelectedAirport()
        val departureDate = binding.departureDateSearchInput
        val returnDate = binding.returnDateSearchInput
        val adultsNumber = adultsDropdown.getValueForSelectedItem(resources.getStringArray(R.array.adults_dropdown_values))
        val bookingClass = bookingClassesDropdown.getValueForSelectedItem(resources.getStringArray(R.array.booking_classes_dropdown_values))
        val checkInDate = binding.checkInDateInput
        val checkOutDate = binding.checkOutDateInput

        // Input validation
        val inputValidation = bookingViewModel.bookingSearchValidation(
            flightHotel= flightHotel,
            oneWay = oneWay,
            departureDate= departureDate,
            returnDate= returnDate,
            checkInDate= checkInDate,
            checkOutDate= checkOutDate
        )

        val airportSelectionValidation = (originCity != null) && (destinationCity != null)

        if (inputValidation && airportSelectionValidation) {

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val newBookingSearch = BookingSearchRequest(
                oneWay= oneWay,
                nonStop= false,
                origin= originCity!!,
                destination=  destinationCity!!,
                departureDate= LocalDate.parse(departureDate.text.toString(), formatter).toString(),
                returnDate= LocalDate.parse(returnDate.text.toString(), formatter).toString(),
                adultsNumber= adultsNumber.toInt(),
                travelClass= bookingClass,
                hotel= flightHotel,
                checkInDate = if (checkInDate.text.isNullOrBlank()) null else LocalDate.parse(checkInDate.text, formatter).toString(),
                checkOutDate = if (checkOutDate.text.isNullOrBlank()) null else LocalDate.parse(checkOutDate.text, formatter).toString()
            )

            bookingViewModel.setBookingSearchRequest(newBookingSearch)
            bookingViewModel.setNewSearch(true)
            findNavController().navigate(R.id.action_bookingSearchFragment_to_predictedBookingFragment)

        } else {
            Snackbar.make(binding.root, "Fields can't be empty." , Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNavigationView).show()
        }
    }
}