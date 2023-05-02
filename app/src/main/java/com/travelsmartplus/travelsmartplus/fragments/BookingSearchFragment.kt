package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentBookingSearchBinding
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale

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

        binding.departureDateSearchInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker(binding.departureDateSearchInput)
            }
        }

        binding.returnDateSearchInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker(binding.returnDateSearchInput)
            }
        }

        // Dropdown Menus
        val adults = resources.getStringArray(R.array.adults_dropdown)
        val bookingClasses = resources.getStringArray(R.array.booking_classes_dropdown)
        setDropdownMenu(binding.adultsDropdownInput, adults)
        setDropdownMenu(binding.bookingClassDropdownInput, bookingClasses)


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
    }

}