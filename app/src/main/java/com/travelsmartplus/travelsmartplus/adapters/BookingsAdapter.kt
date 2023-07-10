package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedDateLong
import kotlinx.datetime.toJavaLocalDate

/**
 * BookingsAdapter
 * Custom adapter to display current user's bookings in [RecyclerView]
 *
 * @author Gabriel Salas
 */

class BookingsAdapter(
    private val bookings: List<Booking>,
    private val listener: OnItemClickListener<Int>
    ) :
    RecyclerView.Adapter<BookingsAdapter.ViewHolder>(), Filterable {

    private var originalBookings: List<Booking> = bookings
    private var filteredBookings: List<Booking> = originalBookings

    // Reference to the type of views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener {

        val destination: TextView = itemView.findViewById(R.id.bookingItemDestination)
        val date: TextView = itemView.findViewById(R.id.bookingItemDate)
        val image: ImageView = itemView.findViewById(R.id.bookingItemImage)

        // Set OnClick listener
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(filteredBookings[position].id!!)
            }
        }

    }

    // Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookingsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_booking, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val booking = filteredBookings[position]

        viewHolder.destination.text = booking.destination.city
        viewHolder.date.text = formattedDateLong(booking.departureDate.toJavaLocalDate())

        // Load the image using Glide
        Glide.with(viewHolder.itemView)
            .load(booking.imageUrl)
            .placeholder(R.drawable.timetable)
            .error(R.drawable.timetable)
            .into(viewHolder.image)
    }

    // Return the size the dataset
    override fun getItemCount() = filteredBookings.size

    // Update adapter data
    fun updateBookings(newBookings: List<Booking>) {
        originalBookings = newBookings
        filteredBookings = newBookings
        notifyDataSetChanged()
    }

    // Filters data
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Filter patterns
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredBookings = originalBookings.filter { booking ->
                    booking.destination.city.lowercase().contains(filterPattern) ||
                            booking.id.toString().contains(filterPattern) ||
                            booking.destination.country.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredBookings
                filterResults.count = filteredBookings.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBookings = if (results != null) {
                    results.values as List<Booking>
                } else {
                    originalBookings
                }
                notifyDataSetChanged()
            }
        }
    }

}