package com.lra.kalanikethan.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.viewmodel.ClassManagementViewModel
import com.lra.kalanikethan.viewmodel.HistoryViewModel
import com.lra.kalanikethan.ui.components.HistoryComponent
import com.lra.kalanikethan.ui.components.SelectionButton2
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import com.lra.kalanikethan.ui.theme.UnselectedChipBackground
import com.lra.kalanikethan.util.formatDateToDayMonth
import com.lra.kalanikethan.util.groupHistoriesByDay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun HistoryContent(
    historyViewModel: HistoryViewModel,
    attendanceViewModel: AttendanceViewModel,
    classViewModel: ClassManagementViewModel,
    modifier: Modifier = Modifier
) {
    val histories by historyViewModel.histories.collectAsState()
    val students by attendanceViewModel.allStudents.collectAsState()
    val employees by historyViewModel.employees.collectAsState()
    val classes by classViewModel.allClasses.collectAsState()

    var selectedClassId by remember { mutableStateOf<Int?>(null) }

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
            .background(Background)
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
                    color = if (selectedClassId == null) AccentColor else UnselectedChipBackground,
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
                    color = if (selectedClassId == eachClass.classId) AccentColor else UnselectedChipBackground,
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
    historyViewModel: HistoryViewModel,
    attendanceViewModel: AttendanceViewModel,
    classViewModel: ClassManagementViewModel
) {
    var showExportSheet by remember { mutableStateOf(false) }

    val students by attendanceViewModel.allStudents.collectAsState()
    val classes by classViewModel.allClasses.collectAsState()

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
            historyViewModel = historyViewModel,
            attendanceViewModel = attendanceViewModel,
            classViewModel = classViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (showExportSheet) {
        ExportHistoryBottomSheet(
            onDismiss = { showExportSheet = false },
            onExport = { from, to ->
                historyViewModel.exportHistoryPdf(from, to, students, classes)
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
