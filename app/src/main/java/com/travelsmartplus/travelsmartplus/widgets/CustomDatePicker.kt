package com.travelsmartplus.travelsmartplus.widgets

import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * CustomDropdown
 * Provides custom DataPickers for the application
 *
 * @author Gabriel Salas
 */

class CustomDatePicker(private val fragmentManager: FragmentManager) {

    // Date range picker
    fun showDateRangePicker(
        startDateEditText: EditText,
        endDateEditText: EditText,
        title: String
    ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(title)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = Date(selection.first)
            val endDate = Date(selection.second)
            val formattedStartDate = dateFormat.format(startDate)
            val formattedEndDate = dateFormat.format(endDate)

            // Update text in text inputs
            startDateEditText.setText(formattedStartDate)
            endDateEditText.setText(formattedEndDate)
        }

        datePicker.show(fragmentManager, "tag")
    }

    // Single date picker
    fun showDatePicker(
        dateEditText: EditText,
        title: String
    ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)

            // Update text in departure date input
            dateEditText.setText(formattedDate)
        }

        datePicker.show(fragmentManager, "tag")
    }

    // Unconstrained date picker - Allows past dates
    fun showPastDatePicker(
        dateEditText: EditText,
        title: String
    ) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)

            // Update text in departure date input
            dateEditText.setText(formattedDate)
        }

        datePicker.show(fragmentManager, "tag")
    }
}