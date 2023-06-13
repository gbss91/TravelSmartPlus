package com.travelsmartplus.travelsmartplus.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Airport

/**
 * AirportAdapter
 * Customer adapter for displaying airports in an AutoCompleteTextView and filter them. Extends [ArrayAdapter]
 *
 * @param context The context.
 * @param airports The list of airports to display.
 * @author Gabriel Salas
 */

class AirportAdapter(context: Context, airports: List<Airport>) :
    ArrayAdapter<Airport>(context, R.layout.airport_autocomplete_item, airports), Filterable {

    private val originalAirports: List<Airport> = airports.toList()
    private var filteredAirports: List<Airport> = originalAirports

    override fun getCount(): Int {
        return filteredAirports.size
    }

    override fun getItem(position: Int): Airport {
        return filteredAirports[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.airport_autocomplete_item, parent, false)
        val airport = getItem(position)

        // Set the airport information in the TextViews
        val airportItemCity = view.findViewById<TextView>(R.id.airportItemCity)
        val airportItemName = view.findViewById<TextView>(R.id.airportItemName)
        val airportItemIata = view.findViewById<TextView>(R.id.airportItemIata)

        airportItemCity.text = context.getString(R.string.city_and_country, airport.city, airport.country)
        airportItemName.text = airport.airportName
        airportItemIata.text = airport.iataCode

        return view
    }

    // Custom filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredAirports = originalAirports.filter { airport ->
                            airport.iataCode.lowercase().contains(filterPattern) ||
                            airport.airportName.lowercase().contains(filterPattern) ||
                            airport.city.lowercase().contains(filterPattern) ||
                            airport.country.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredAirports
                filterResults.count = filteredAirports.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredAirports = if (results != null) {
                    results.values as List<Airport>
                } else {
                    originalAirports
                }
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                // Convert the selected item to a string representation
                return if (resultValue is Airport) {
                    "${resultValue.city}, ${resultValue.country}"
                } else {
                    super.convertResultToString(resultValue)
                }
            }
        }
    }
}
