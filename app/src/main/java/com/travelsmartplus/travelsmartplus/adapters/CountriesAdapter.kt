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

/**
 * CountriesAdapter
 * Custom adapter to display countries in AutoCompleteTextView. Extends [ArrayAdapter]
 *
 * @author Gabriel Salas
 */

class CountriesAdapter(context: Context, countries: List<String>) :
    ArrayAdapter<String>(context, R.layout.item_dropdown, countries), Filterable {

    private val originalCountries: List<String> = countries
    private var filteredCountries: List<String> = originalCountries

    override fun getCount(): Int {
        return filteredCountries.size
    }

    override fun getItem(position: Int): String {
        return filteredCountries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        val country = getItem(position)

        // Set the country name in the TextView
        val itemName = view.findViewById<TextView>(R.id.textViewDropdown)
        itemName.text = country

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Filter patterns
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredCountries = originalCountries.filter { country ->
                    country.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredCountries
                filterResults.count = filteredCountries.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCountries = if (results != null) {
                    results.values as List<String>
                } else {
                    originalCountries
                }
                notifyDataSetChanged()
            }
        }
    }
}