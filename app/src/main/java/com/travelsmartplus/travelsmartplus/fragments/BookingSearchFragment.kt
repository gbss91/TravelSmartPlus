package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingSearchBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * BookingSearchFragment
 * Fragment for searching flights and hotels.
 *
 * @author Gabriel Salas
 */

class BookingSearchFragment : Fragment() {

    private lateinit var binding: FragmentBookingSearchBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable Departure and Return field - Can only be updated via date picker
        binding.departureDateSearchInput.isFocusable = false
        binding.departureDateSearchInput.isFocusableInTouchMode = false
        binding.returnDateSearchInput.isFocusable = false
        binding.returnDateSearchInput.isFocusableInTouchMode = false

        binding.departureDateSearchInput.setOnClickListener {
            showDatePicker(binding.departureDateSearchInput)
        }

        binding.returnDateSearchInput.setOnClickListener {
            showDatePicker(binding.returnDateSearchInput)
        }

        // Dropdown Menus
        val adults = resources.getStringArray(R.array.adults_dropdown)
        val bookingClasses = resources.getStringArray(R.array.booking_classes_dropdown)
        setDropdownMenu(binding.adultsDropdownInput, adults)
        setDropdownMenu(binding.bookingClassDropdownInput, bookingClasses)


    }

    /* private fun bookingSearch(): Boolean {

        val originCity = binding.fromSearchInput
        val destinationCity = binding.toSearchInput
        val departureDate = binding.departureDateSearchInput
        val returnDate = binding.returnDateSearchInput
        val adultsNumber = binding.adultsDropdownInput
        val bookingClass = binding.bookingClassDropdownInput

        // Input validation
        var inputValidation = {
            originCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { originCity.error = it }.check()
            destinationCity.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { destinationCity.error = it }.check()
            departureDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { departureDate.error = it }.check()
            returnDate.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { returnDate.error = it }.check()
            adultsNumber.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { adultsNumber.error = it }.check()
            bookingClass.validator().nonEmpty().addRule(NotBlankRule()).addErrorCallback { bookingClass.error = it }.check()

            // Return if not errors
            originCity.error == null && destinationCity.error == null && departureDate.error == null &&returnDate.error == null
                    && adultsNumber.error == null && bookingClass.error == null
        }

        if (inputValidation()) {
            TODO()
        }
    }*/

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
    }

}