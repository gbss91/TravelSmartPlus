package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.HotelBooking

/**
 * HotelResultsAdapter
 * Custom adapter to display hotel result list in [RecyclerView]
 *
 * @author Gabriel Salas
 */

class HotelResultsAdapter(
    private val hotels: List<HotelBooking>,
    private val listener: OnItemClickListener<HotelBooking>
    ) :
    RecyclerView.Adapter<HotelResultsAdapter.ViewHolder>() {

    // Reference to the type of views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener {

        val hotelRate: TextView = itemView.findViewById(R.id.hotelItemPriceText)
        val hotelDetails: ConstraintLayout = itemView.findViewById(R.id.hotelItemDetails)

        // Set OnClick listener
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(hotels[position])
            }
        }
    }

    // Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HotelResultsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_hotel_result, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val hotelOffer = hotels[position]

        viewHolder.hotelRate.text = viewHolder.itemView.context.getString(R.string.currency_price, hotelOffer.rate.toInt().toString())
        viewHolder.hotelDetails.findViewById<TextView>(R.id.hotelItemNameText).text = hotelOffer.hotelName
        viewHolder.hotelDetails.findViewById<TextView>(R.id.hotelItemAddressText).text = hotelOffer.address
        viewHolder.hotelDetails.findViewById<TextView>(R.id.hotelItemRoomText).text = hotelOffer.roomType ?: "" // Show empty string if null
    }

    // Return the size the dataset
    override fun getItemCount() = hotels.size

}