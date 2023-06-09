package com.travelsmartplus.travelsmartplus.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Airport

/**
 * CustomDropdown
 * Provides custom Dropdowns for the application
 *
 * @author Gabriel Salas
 */

class CustomDropdown {

    private var selectedItemIndex: Int = 0

    // A simple dropdown with preselected option
    fun setSimpleDropdown(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        items: Array<String>
    ) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, items)
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedItemIndex = position
        }

        autoCompleteTextView.setText(items[0], false) // Set the first item as preselected
    }

    // A dropdown that accepts a callback function to perform another action
    fun setActionDropdown(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        items: Array<String>,
        onItemSelected: (selectedItem: String) -> Unit
    ) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, items)
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            onItemSelected(selectedItem)
            selectedItemIndex = position
        }

        autoCompleteTextView.setText(items[0], false) // Set the first item as preselected
    }

    // Autocomplete dropdown
    fun setAirportAutocomplete(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        airports: List<Airport>
    ) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, airports.map { it.airportName })
        autoCompleteTextView.threshold = 3 // Starts with 3 characters
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedItemIndex = position
        }
    }

    // Gets the corresponding value for the selected item - Allows item text and value to be different
    fun getValueForSelectedItem(values: Array<String>): String {
        return values[selectedItemIndex]
    }

}