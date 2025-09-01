package com.lra.kalanikethan.util

fun formatDateToDayMonth(dateString: String): String {
    val parts = dateString.split("-")
    if (parts.size != 3) return dateString // Return original if format is invalid

    val year = parts[0].toInt()
    val month = parts[1].toInt()
    val day = parts[2].toInt()

    val daySuffix = when (day) {
        1, 21, 31 -> "${day}st"
        2, 22 -> "${day}nd"
        3, 23 -> "${day}rd"
        else -> "${day}th"
    }

    val monthName = when (month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Unknown"
    }

    return "$daySuffix $monthName"
}