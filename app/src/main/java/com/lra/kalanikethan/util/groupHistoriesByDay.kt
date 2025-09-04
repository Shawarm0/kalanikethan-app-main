package com.lra.kalanikethan.util

import android.util.Log
import com.lra.kalanikethan.data.models.History
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * Groups histories by day and sorts them by sign-out time.
 *
 * @param histories The list of histories to group.
 * @return A list of lists, where each inner list contains histories for a specific day.
 */
fun groupHistoriesByDay(histories: List<History>): List<List<History>> {
    return histories
        .groupBy { it.date }
        .toList()
        .sortedByDescending { (date, _) ->
            try {
                LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            } catch (e: Exception) {
                Log.e("GroupHistoriesByDay", "Error parsing date: $date", e)
                LocalDate.MIN
            }
        }
        .map { (_, dayHistories) ->
            dayHistories.sortedByDescending { history ->
                history.signOutTime ?: Long.MAX_VALUE
            }
        }
}