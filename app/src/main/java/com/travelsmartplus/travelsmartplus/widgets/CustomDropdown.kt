package com.travelsmartplus.travelsmartplus.widgets

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.AirlinePreferencesAdapter
import com.travelsmartplus.travelsmartplus.adapters.AirportAdapter
import com.travelsmartplus.travelsmartplus.adapters.HotelPreferencesAdapter
import com.travelsmartplus.travelsmartplus.data.models.Airline
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Hotel

/**
 * CustomDropdown
 * Provides custom Dropdowns for the application
 *
 * @author Gabriel Salas
 */

class CustomDropdown {

    private var selectedItemIndex: Int = 0
    private var selectedAirport: Airport? = null
    private var selectedHotel: Hotel? = null
    private var selectedAirline: Airline? = null

    // A simple dropdown with preselected option
    fun setSimpleDropdown(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        items: Array<String>
    ) {
        val adapter = ArrayAdapter(context, R.layout.item_dropdown, items)
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
        val adapter = ArrayAdapter(context, R.layout.item_dropdown, items)
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            onItemSelected(selectedItem)
            selectedItemIndex = position
        }

        autoCompleteTextView.setText(items[0], false) // Set the first item as preselected
    }

    // Airport Autocomplete dropdown
    fun setAirportAutocomplete(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        airports: List<Airport>
    ) {
        val adapter = AirportAdapter(context, airports)
        autoCompleteTextView.threshold = 3 // Starts with 3 characters
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedAirport = adapter.getItem(position)
        }
    }

    // Airline Autocomplete dropdown
    fun setAirlineAutoComplete(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        airlines: List<Airline>
    ) {
        val adapter = AirlinePreferencesAdapter(context, airlines)
        autoCompleteTextView.threshold = 2
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedAirline = adapter.getItem(position)
        }

    }

    // Hotel Autocomplete dropdown
    fun setHotelAutoComplete(
        context: Context,
        autoCompleteTextView: AutoCompleteTextView,
        hotels: List<Hotel>
    ) {
        val adapter = HotelPreferencesAdapter(context, hotels)
        autoCompleteTextView.threshold = 2
        autoCompleteTextView.setAdapter(adapter)

        // Set selected item index
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedHotel = adapter.getItem(position)
        }

    }

    // Gets the corresponding value for the selected item - Allows item text and value to be different
    fun getValueForSelectedItem(values: Array<String>): String {
        return values[selectedItemIndex]
    }

    fun getSelectedAirport(): Airport? {
        return selectedAirport
    }

    fun getSelectedAirline(): Airline? {
        return selectedAirline
    }

    fun getSelectedHotel(): Hotel? {
        return selectedHotel
    }


}