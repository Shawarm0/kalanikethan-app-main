package com.lra.kalanikethan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.ui.components.HistoryComponent
import com.lra.kalanikethan.ui.screens.SignIn.SignInViewModel
import com.lra.kalanikethan.util.formatDateToDayMonth
import com.lra.kalanikethan.util.groupHistoriesByDay

@Composable
fun History(viewModel: SignInViewModel) {

    val students by viewModel.allStudents. collectAsState(emptyList())
    val employees by viewModel.employees.collectAsState(emptyList())
    val histories by viewModel.histories.collectAsState(emptyList())

    // Create a map for quick student lookup by ID
    val studentMap = remember(students) {
        students.associateBy { it.studentId }
    }
    val employeeMap = remember(employees) {
        employees.associateBy { it.uid }
    }

    LaunchedEffect(Unit) {
        viewModel.initialiseHistoryChannel()
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val groupedByDay = groupHistoriesByDay(histories)

        items(groupedByDay) { dayList ->
            val day = formatDateToDayMonth(dayList[0].date)

            HistoryComponent(
                day = day,
                data = dayList,
                studentMap = studentMap,
                employeeMap = employeeMap
            )
        }



//        groupedByDay.forEach { dayList ->
//
//            val day = formatDateToDayMonth(dayList[0].date)
//
//            HistoryComponent(
//                day = day,
//                data = dayList,
//                studentMap = studentMap,
//                employeeMap = employeeMap
//            )
//        }
    }
}

