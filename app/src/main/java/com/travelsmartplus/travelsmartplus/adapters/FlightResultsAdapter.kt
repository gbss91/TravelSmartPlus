package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedDuration
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedStops
import com.travelsmartplus.travelsmartplus.utils.Formatters.formattedTime


/**
 * FlightResultsAdapter
 * Custom adapter to display flight result list in [RecyclerView]
 *
 * @author Gabriel Salas
 */

class FlightResultsAdapter(private val flights: List<FlightBooking>) :
    RecyclerView.Adapter<FlightResultsAdapter.ViewHolder>() {

    // Reference to the type of views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val outboundFlight: View = itemView.findViewById(R.id.flightResultOutbound)
        val inboundFlight: View = itemView.findViewById(R.id.flightResultInbound)
        val price: TextView = itemView.findViewById(R.id.predictedBookingPriceText)
    }

    // Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FlightResultsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_flight_result, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flightOffer = flights[position]
        val outbound = flightOffer.segments[0]

        // Set outbound details
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemAirline).text = outbound.flights[0].carrierName
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemDepartureTimeText).text = formattedTime(outbound.flights[0].departureTime)
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemOriginIataText).text = outbound.flights[0].departureAirport.iataCode
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemDuration).text = formattedDuration(outbound.duration, viewHolder.itemView.context)
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemStops).text = formattedStops(outbound)
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemArrivalTimeText).text = formattedTime(outbound.flights.last().arrivalTime)
        viewHolder.outboundFlight.findViewById<TextView>(R.id.flightItemDestinationIataText).text = outbound.flights.last().arrivalAirport.iataCode

        // Shows inbound section for return flights
        if (!flightOffer.oneWay) {
            val inbound = flightOffer.segments[1]

            viewHolder.inboundFlight.visibility = View.VISIBLE
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemAirline).text = inbound.flights[0].carrierName
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemDepartureTimeText).text = formattedTime(inbound.flights[0].departureTime)
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemOriginIataText).text = inbound.flights[0].departureAirport.iataCode
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemDuration).text = formattedDuration(inbound.duration, viewHolder.itemView.context)
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemStops).text = formattedStops(inbound)
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemArrivalTimeText).text = formattedTime(inbound.flights.last().arrivalTime)
            viewHolder.inboundFlight.findViewById<TextView>(R.id.flightItemDestinationIataText).text = inbound.flights.last().arrivalAirport.iataCode

        } else {
            viewHolder.inboundFlight.visibility = View.GONE
        }

        viewHolder.price.text = viewHolder.itemView.context.getString(R.string.currency_price, flightOffer.totalPrice.toInt().toString())

    }

    // Return the size the dataset
    override fun getItemCount() = flights.size

}