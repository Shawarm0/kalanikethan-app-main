package com.lra.kalanikethan.util

import java.text.SimpleDateFormat

import java.util.Date

fun convertLongToTime(time: Long?): String? {
    if (time == null) return null else {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}