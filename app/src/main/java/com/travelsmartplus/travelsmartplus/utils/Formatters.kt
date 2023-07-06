package com.travelsmartplus.travelsmartplus.utils

import android.content.Context
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.FlightSegment
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Formatters
 * Transforms data to required format
 *
 * @author Gabriel Salas
 */

object Formatters {

    fun formattedStops(flightSegment: FlightSegment): String {
        return when(flightSegment.stops) {
            0 -> return "Direct"
            1 -> return "1 Stop"
            else -> "${flightSegment.stops} Stops"
        }
    }

    fun formattedTime(time: LocalDateTime): String {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun formattedDuration(duration: Duration, context: Context): String {
        val hours = duration.toHours();
        val minutes = duration.toMinutes() % 60;
        return  context.getString(R.string.formatted_duration, hours, minutes)
    }

    fun formattedDateLong(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
    }

    fun formattedDateMedium(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    fun formattedDateShort(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}