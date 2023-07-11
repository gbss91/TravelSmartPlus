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
import com.travelsmartplus.travelsmartplus.data.models.Hotel

/**
 * HotelPreferencesAdapter
 * Custom adapter to display hotels in AutoCompleteTextView. Extends [ArrayAdapter]
 *
 * @author Gabriel Salas
 */

class HotelPreferencesAdapter(context: Context, hotels: List<Hotel>) :
    ArrayAdapter<Hotel>(context, R.layout.item_dropdown, hotels), Filterable {

    private val originalHotels: List<Hotel> = hotels.toList()
    private var filteredHotels: List<Hotel> = originalHotels

    override fun getCount(): Int {
        return filteredHotels.size
    }

    override fun getItem(position: Int): Hotel {
        return filteredHotels[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        val hotel = getItem(position)

        // Set the hotel information in the TextViews
        val itemName = view.findViewById<TextView>(R.id.textViewDropdown)
        itemName.text = hotel.hotelChain

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Filter patterns
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredHotels = originalHotels.filter { hotel ->
                    hotel.hotelChain.lowercase().contains(filterPattern) || hotel.code.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredHotels
                filterResults.count = filteredHotels.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredHotels = if (results != null) {
                    results.values as List<Hotel>
                } else {
                    originalHotels
                }
                notifyDataSetChanged()
            }

            // Display only Hotel Chain Name when selected
            override fun convertResultToString(resultValue: Any?): CharSequence {
                // Convert the selected item to a string representation
                return if (resultValue is Hotel) {
                    resultValue.hotelChain
                } else {
                    super.convertResultToString(resultValue)
                }
            }

        }
    }

}