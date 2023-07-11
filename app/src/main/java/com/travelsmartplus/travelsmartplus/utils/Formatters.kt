package com.travelsmartplus.travelsmartplus.utils

import android.content.Context
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.FlightSegment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

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

    fun formattedArrivalTime(departureTime: LocalDateTime, arrivalTime: LocalDateTime): String {
        val formattedArrival = arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val daysDifference = ChronoUnit.DAYS.between(departureTime.toLocalDate(), arrivalTime.toLocalDate())

        return when {
            daysDifference > 0 -> formattedArrival +  "\u207A"
            daysDifference < 0 -> formattedArrival +  "\u207B"
            else -> formattedArrival
        }
    }

    fun formattedDuration(duration: Duration, context: Context): String {
        val hours = duration.inWholeHours
        val minutes = (duration - hours.hours).inWholeMinutes
        return context.getString(R.string.formatted_duration, hours, minutes)
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