package com.lra.kalanikethan.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converts a long timestamp to a formatted time string.
 *
 * @param time The long timestamp to convert.
 */
fun convertLongToTime(time: Long?): String? {
    if (time == null) return null else {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }
}