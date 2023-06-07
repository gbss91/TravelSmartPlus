package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingSearchBinding
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * BookingSearchFragment
 * Fragment for searching flights and hotels.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class BookingSearchFragment : Fragment() {

    private lateinit var binding: FragmentBookingSearchBinding
    private val bookingViewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingSearchBinding.inflate(inflater, container, false)

        // Disable date fields - Can only be updated via date picker
        binding.departureDateSearchInput.isFocusable = false
        binding.departureDateSearchInput.isFocusableInTouchMode = false
        binding.returnDateSearchInput.isFocusable = false
        binding.returnDateSearchInput.isFocusableInTouchMode = false
        binding.checkInDateInput.isFocusable = false
        binding.checkInDateInput.isFocusableInTouchMode = false
        binding.checkOutDateInput.isFocusable = false
        binding.checkOutDateInput.isFocusableInTouchMode = false

        // Set flight only button checked and remove hotel fields as default state
        binding.searchToggleGroup.check(R.id.flightOnlySearchBtn)
        binding.checkInDateContainer.visibility = View.GONE
        binding.checkOutDateContainer.visibility = View.GONE

        // Set Dropdown Menus
        val onewayDropdown = resources.getStringArray(R.array.oneway_dropdown)
        val adultsDropdown = resources.getStringArray(R.array.adults_dropdown)
        val bookingClassesDropdown = resources.getStringArray(R.array.booking_classes_dropdown)
        setDropdownMenu(binding.onewayDropdownInput, onewayDropdown)
        setDropdownMenu(binding.adultsDropdownInput, adultsDropdown)
        setDropdownMenu(binding.bookingClassDropdownInput, bookingClassesDropdown)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toggle button functionality
        binding.searchToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.flightOnlySearchBtn -> {
                        binding.checkInDateContainer.visibility = View.GONE
                        binding.checkOutDateContainer.visibility = View.GONE
                    }
                    R.id.flightHotelSearchBtn -> {
                        binding.checkInDateContainer.visibility = View.VISIBLE
                        binding.checkOutDateContainer.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.departureDateSearchInput.setOnClickListener {
            showDatePicker(binding.departureDateSearchInput)
        }

        binding.returnDateSearchInput.setOnClickListener {
            showDatePicker(binding.returnDateSearchInput)
        }

        binding.checkInDateInput.setOnClickListener {
            showDatePicker(binding.checkInDateInput)
        }

        binding.checkOutDateInput.setOnClickListener {
            showDatePicker(binding.checkOutDateInput)
        }

        binding.searchBtn.setOnClickListener {
            bookingSearch()
        }


    }

    private fun bookingSearch(): Boolean {

        val originCity = binding.fromSearchInput
        val destinationCity = binding.toSearchInput
        val departureDate = binding.departureDateSearchInput
        val returnDate = binding.returnDateSearchInput
        val adultsNumber = binding.adultsDropdownInput
        val bookingClass = binding.bookingClassDropdownInput
        val checkInDate = binding.checkInDateInput
        val checkOutDate = binding.checkOutDateInput

        // Input validation
        var inputValidation = bookingViewModel.bookingSearchValidation(
            flightOnly= true,
            originCity= originCity,
            destinationCity= destinationCity,
            departureDate= departureDate,
            returnDate= returnDate,
            checkInDate= checkInDate,
            checkOutDate= checkOutDate
        )

        if (inputValidation) {
            return false
        } else {
            val snackBar = Snackbar.make(binding.root, "Fields can't be empty." , Snackbar.LENGTH_SHORT)
            snackBar.setAnchorView(R.id.bottomNavigationView).show()
        }
        return false
    }



    private fun showDatePicker(editText: EditText) {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Departure date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = Date(it)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = simpleDateFormat.format(selectedDate)

            // Update text in text input
            editText.setText(formattedDate)
        }

        datePicker.show(childFragmentManager, "tag")
    }

    private fun setDropdownMenu(autoCompleteTextView: AutoCompleteTextView, items: Array<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setText(items[0], false) // Set the first item as preselected
    }

}