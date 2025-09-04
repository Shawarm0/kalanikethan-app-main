package com.lra.kalanikethan.util

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

/**
 * Checks if the current time is within the specified range.
 *
 * @param startMillis The start time in milliseconds.
 * @param endMillis The end time in milliseconds.
 * @return `true` if the current time is within the specified range, `false` otherwise.
 */
fun isCurrentTimeWithinRange(startMillis: Long, endMillis: Long): Boolean {
    val zoneId = ZoneId.systemDefault()

    val startTime = Instant.ofEpochMilli(startMillis)
        .atZone(zoneId)
        .toLocalTime()

    val endTime = Instant.ofEpochMilli(endMillis)
        .atZone(zoneId)
        .toLocalTime()

    val now = LocalTime.now(zoneId)

    return now.isAfter(startTime) && now.isBefore(endTime)
}