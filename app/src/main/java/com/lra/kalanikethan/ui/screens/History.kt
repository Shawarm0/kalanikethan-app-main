package com.lra.kalanikethan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.ui.components.HistoryComponent
import com.lra.kalanikethan.ui.components.SelectionButton
import com.lra.kalanikethan.ui.components.SelectionButton2
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.DashBoardViewModel
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import com.lra.kalanikethan.util.formatDateToDayMonth
import com.lra.kalanikethan.util.groupHistoriesByDay
import io.github.jan.supabase.realtime.Column

/**
 * A composable function that displays a history list of sign-in events.
 *
 * This component displays a chronological list of sign-in histories grouped by day,
 * showing student and employee information for each event. It collects data from
 * the provided [SignInViewModel] and organizes histories into daily sections.
 *
 * @param viewModel The [SignInViewModel] that provides the data streams for students,
 * employees, and history records. The viewModel is responsible for fetching and
 * managing the historical sign-in data.
 *
 * The composable performs the following operations:
 * - Collects lists of students, employees, and histories from the viewModel
 * - Creates lookup maps for efficient student and employee data retrieval
 * - Initializes the history channel on first composition
 * - Groups histories by day and displays them in a lazy column
 *
 * @see HistoryComponent for the individual history item rendering
 */
@Composable
fun History(
    viewModel: SignInViewModel,
    dashBoardViewModel: DashBoardViewModel
) {

    val students by viewModel.allStudents.collectAsState(emptyList())
    val employees by viewModel.employees.collectAsState(emptyList())
    val histories by viewModel.histories.collectAsState(emptyList())
    val classes by dashBoardViewModel.allClasses.collectAsState(emptyList())


    var selectedClassId by remember { mutableStateOf<Int?>(null) }

    val filteredStudents by dashBoardViewModel.getStudentsForClassFlow(selectedClassId ?: -5)
        .collectAsState(initial = emptyList())

    val filteredHistories = remember(histories, filteredStudents, selectedClassId) {
        if (selectedClassId != null) {
            // Only show histories for students in the filtered list
            histories.filter { history ->
                filteredStudents.any { student ->
                    student.studentId == history.studentID
                }
            }
        } else {
            // Show all histories when no class is selected
            histories
        }
    }

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


    Column(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LazyRow(
            modifier = Modifier.width(1075.dp)
        ) {
            // Add "All Classes" option
            item {
                SelectionButton2(
                    text = "All Classes",
                    onClick = {
                        selectedClassId = null
                    },
                    color = if (selectedClassId == null) AccentColor else Color(0xFFE7EEF5),
                    textColor = if (selectedClassId == null) Color(0xFFFFFFFF) else UnselectedButtonText
                )
            }
            items(classes) { eachclass ->
                SelectionButton2(
                    text = eachclass.teacherName,
                    onClick = {
                        selectedClassId = eachclass.classId
                    },
                    color = if (selectedClassId == eachclass.classId) AccentColor else Color(0xFFE7EEF5),
                    textColor = if (selectedClassId == eachclass.classId) Color(0xFFFFFFFF) else UnselectedButtonText
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 12.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val groupedByDay = groupHistoriesByDay(filteredHistories)

            if (groupedByDay.isEmpty()) {
                item {
                    Text(
                        text = if (selectedClassId != null) {
                            "No history records for selected class"
                        } else {
                            "No history records found"
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(groupedByDay) { dayList ->
                    val day = formatDateToDayMonth(dayList[0].date)
                    HistoryComponent(
                        day = day,
                        data = dayList,
                        studentMap = studentMap,
                        employeeMap = employeeMap
                    )
                }
            }
        }
    }
}

