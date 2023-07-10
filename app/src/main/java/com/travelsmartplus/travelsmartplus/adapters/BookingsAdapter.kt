package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
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
    RecyclerView.Adapter<BookingsAdapter.ViewHolder>() {

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
                listener.onItemClick(bookings[position].id!!)
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
        val booking = bookings[position]

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
    override fun getItemCount() = bookings.size

}