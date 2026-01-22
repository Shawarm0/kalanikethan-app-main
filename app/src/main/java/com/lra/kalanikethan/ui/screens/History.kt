package com.lra.kalanikethan.ui.screens

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.lra.kalanikethan.StudentsViewModel
import com.lra.kalanikethan.ui.components.HistoryComponent
import com.lra.kalanikethan.ui.components.SelectionButton
import com.lra.kalanikethan.ui.components.SelectionButton2
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.DashBoardViewModel
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import com.lra.kalanikethan.util.formatDateToDayMonth
import com.lra.kalanikethan.util.groupHistoriesByDay
import io.github.jan.supabase.realtime.Column
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

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
fun HistoryContent(
    viewModel: StudentsViewModel,
    modifier: Modifier = Modifier
) {
    // 👈 your existing History UI goes here
    val histories by viewModel.histories.collectAsState(emptyList())
    val students by viewModel.allStudents.collectAsState(emptyList())
    val employees by viewModel.employees.collectAsState(emptyList())
    val classes by viewModel.allClasses.collectAsState(emptyList())
    val studentIdsByClass by viewModel.studentIdsByClass.collectAsState()


    var selectedClassId by remember { mutableStateOf<Int?>(null) }

    // Build lookup maps once
    val studentMap = remember(students) {
        students.associateBy { it.studentId }
    }
    val employeeMap = remember(employees) {
        employees.associateBy { it.uid }
    }

    val filteredHistories = remember(
        histories,
        selectedClassId
    ) {
        if (selectedClassId == null) {
            histories
        } else {
            histories.filter { it.classID == selectedClassId }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background) // <-- add this
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.width(1075.dp)
        ) {
            item {
                SelectionButton2(
                    text = "All Classes",
                    onClick = { selectedClassId = null },
                    color = if (selectedClassId == null) AccentColor else Color(0xFFE7EEF5),
                    textColor = if (selectedClassId == null) Color.White else UnselectedButtonText
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }



            items(classes) { eachClass ->
                SelectionButton2(
                    text = eachClass.teacherName,
                    onClick = {
                        selectedClassId = eachClass.classId
                    },
                    color = if (selectedClassId == eachClass.classId) AccentColor else Color(0xFFE7EEF5),
                    textColor = if (selectedClassId == eachClass.classId) Color.White else UnselectedButtonText
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val groupedByDay = groupHistoriesByDay(filteredHistories)

            if (groupedByDay.isEmpty()) {
                item {
                    Text(
                        text = if (selectedClassId != null)
                            "No history records for selected class"
                        else
                            "No history records found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(groupedByDay) { dayList ->
                    HistoryComponent(
                        day = formatDateToDayMonth(dayList.first().date),
                        data = dayList,
                        studentMap = studentMap,
                        employeeMap = employeeMap
                    )
                }
            }
        }
    }
}


@Composable
fun History(
    viewModel: StudentsViewModel
) {
    var showExportSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showExportSheet = true },
                containerColor = AccentColor
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Export PDF",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        HistoryContent(
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (showExportSheet) {
        ExportHistoryBottomSheet(
            onDismiss = { showExportSheet = false },
            onExport = { from, to ->
                viewModel.exportHistoryPdf(from, to)
                showExportSheet = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportHistoryBottomSheet(
    onDismiss: () -> Unit,
    onExport: (LocalDate, LocalDate) -> Unit
) {
    var fromDate by remember { mutableStateOf<LocalDate?>(null) }
    var toDate by remember { mutableStateOf<LocalDate?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Export History", style = MaterialTheme.typography.titleLarge)

            DatePickerField(
                label = "From Date",
                onDateSelected = { fromDate = it }
            )

            DatePickerField(
                label = "To Date",
                onDateSelected = { toDate = it }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = fromDate != null && toDate != null,
                onClick = {
                    onExport(fromDate!!, toDate!!)
                }
            ) {
                Text("Save PDF")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    onDateSelected: (LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    OutlinedButton(
        onClick = { showPicker = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label)
    }

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            onDateSelected(date)
                        }
                        showPicker = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}





