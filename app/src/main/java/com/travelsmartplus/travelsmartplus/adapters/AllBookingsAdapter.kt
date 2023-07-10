package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.utils.Formatters
import kotlinx.datetime.toJavaLocalDate

/**
 * AllBookingsAdapter
 * Custom adapter to display bookings in [RecyclerView]
 *
 * @author Gabriel Salas
 */

class AllBookingsAdapter(
    private val bookings: List<Booking>,
    private val listener: OnItemClickListener<Int>
) :
    RecyclerView.Adapter<AllBookingsAdapter.ViewHolder>()
{
    // Reference to the type of views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val bookingId: TextView = itemView.findViewById(R.id.bookingRowId)
        val destination: TextView = itemView.findViewById(R.id.bookingRowDestination)
        val departureDate: TextView = itemView.findViewById(R.id.bookingRowDepartureDate)
        val totalPrice: TextView = itemView.findViewById(R.id.bookingRowTotal)

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

    // Inflate the view
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AllBookingsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_booking_row, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val booking = bookings[position]

        viewHolder.bookingId.text = booking.id.toString()
        viewHolder.destination.text = booking.destination.city
        viewHolder.departureDate.text = Formatters.formattedDateShort(booking.departureDate.toJavaLocalDate())
        viewHolder.totalPrice.text = viewHolder.itemView.context.getString(R.string.currency_price, booking.totalPrice)
    }

    // Return the size the dataset
    override fun getItemCount() = bookings.size


}