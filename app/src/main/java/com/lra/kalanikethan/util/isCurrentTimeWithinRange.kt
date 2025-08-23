package com.lra.kalanikethan.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
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