package com.travelsmartplus.travelsmartplus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Airline

/**
 * AirlinePreferencesAdapter
 * Custom adapter to display airlines in AutoCompleteTextView. Extends [ArrayAdapter]
 *
 * @author Gabriel Salas
 */


class AirlinePreferencesAdapter(context: Context, airlines: List<Airline>) :
    ArrayAdapter<Airline>(context, R.layout.item_dropdown, airlines), Filterable {

    private val originalAirlines: List<Airline> = airlines.toList()
    private var filteredAirlines: List<Airline> = originalAirlines

    override fun getCount(): Int {
        return filteredAirlines.size
    }

    override fun getItem(position: Int): Airline {
        return filteredAirlines[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        val airline = getItem(position)

        // Set the airline information in the TextViews
        val itemName = view.findViewById<TextView>(R.id.textViewDropdown)
        itemName.text = airline.airlineName

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Filter patterns
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredAirlines = originalAirlines.filter { airline ->
                    airline.airlineName.lowercase().contains(filterPattern) || airline.iataCode.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredAirlines
                filterResults.count = filteredAirlines.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredAirlines = if (results != null) {
                    results.values as List<Airline>
                } else {
                    originalAirlines
                }
                notifyDataSetChanged()
            }

            // Display only Airline Name when selected
            override fun convertResultToString(resultValue: Any?): CharSequence {
                // Convert the selected item to a string representation
                return if (resultValue is Airline) {
                    resultValue.airlineName
                } else {
                    super.convertResultToString(resultValue)
                }
            }

        }
    }




}