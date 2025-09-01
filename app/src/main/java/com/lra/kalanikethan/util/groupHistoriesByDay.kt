package com.lra.kalanikethan.util

import com.lra.kalanikethan.data.models.History
import java.time.LocalDate
import java.time.format.DateTimeFormatter



fun groupHistoriesByDay(histories: List<History>): List<List<History>> {
    return histories
        .groupBy { it.date }
        .toList()
        .sortedByDescending { (date, _) ->
            try {
                LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            } catch (e: Exception) {
                LocalDate.MIN
            }
        }
        .map { (_, dayHistories) ->
            dayHistories.sortedByDescending { history ->
                history.signOutTime ?: Long.MAX_VALUE
            }
        }
}