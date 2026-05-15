package com.lra.kalanikethan.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.navigation.Screen
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.viewmodel.ClassManagementViewModel
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.ClassStudentComposable
import com.lra.kalanikethan.ui.components.ClassStudentDisplay
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField2
import com.lra.kalanikethan.ui.theme.SuccessColor

@Composable
fun EditClass(
    classViewModel: ClassManagementViewModel,
    attendanceViewModel: AttendanceViewModel,
    navController: NavHostController
) {
    val thisClass = classViewModel.thisClass.collectAsState().value ?: return

    var teacherName by remember(thisClass.classId) { mutableStateOf(thisClass.teacherName) }
    var classType by remember(thisClass.classId) { mutableStateOf(thisClass.type) }
    var startTime by remember(thisClass.classId) { mutableStateOf(thisClass.startTime) }
    var endTime by remember(thisClass.classId) { mutableStateOf(thisClass.endTime) }

    val studentsInClass =
        classViewModel.selectedStudentsForActiveClass.collectAsState().value

    val allStudents = attendanceViewModel.displayedStudents.collectAsState(emptyList())
    val searchQuery by attendanceViewModel.searchQuery.collectAsState()

    LaunchedEffect(thisClass.classId) {
        attendanceViewModel.removeSearchQuery()
        classViewModel.resetPendingUpdates()
        classViewModel.loadStudentsForClass(thisClass.classId)
    }

    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Column(
                modifier = Modifier.padding(start = 53.dp, top = 14.dp)
            ) {
                Text(
                    "${teacherName}'s Class",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .onGloballyPositioned {
                            textWidth = with(density) { it.size.width.toDp() }
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(Modifier.width(textWidth))
                Spacer(Modifier.height(14.dp))
            }

            Column(modifier = Modifier.padding(top = 15.dp)) {

                Row(
                    modifier = Modifier.padding(start = 53.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimpleDecoratedTextField(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = teacherName,
                        label = "Teacher Name",
                        placeholder = "Enter Teacher Name",
                        onValueChange = { teacherName = it }
                    )

                    Spacer(Modifier.width(25.dp))

                    SimpleDecoratedTextField2(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = startTime,
                        label = "Start Time",
                        placeholder = "Enter Start Time",
                        leadingIcon = Icons.Default.AccessTime,
                        onValueChange = { startTime = it }
                    )
                }

                Spacer(Modifier.height(13.dp))

                Row(
                    modifier = Modifier.padding(start = 53.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimpleDecoratedTextField(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = classType,
                        label = "Type",
                        placeholder = "Enter Type",
                        onValueChange = { classType = it }
                    )

                    Spacer(Modifier.width(25.dp))

                    SimpleDecoratedTextField2(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = endTime,
                        label = "End Time",
                        placeholder = "Enter End Time",
                        leadingIcon = Icons.Default.AccessTime,
                        onValueChange = { endTime = it }
                    )
                }
            }

            Column(
                modifier = Modifier.padding(start = 53.dp, top = 19.dp)
            ) {
                Text(
                    "Students",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(Modifier.width(textWidth))
                Spacer(Modifier.height(14.dp))

                val sortedClassStudents = remember(studentsInClass) { studentsInClass.sortedBy { it.firstName } }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(681.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sortedClassStudents, key = { it.studentId ?: 0 }) { student ->
                        ClassStudentDisplay(
                            modifier = Modifier.fillMaxWidth(),
                            name = "${student.firstName} ${student.lastName}"
                        )
                    }
                }
            }
        }

        VerticalDivider(modifier = Modifier.height(400.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 100.dp, end = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SimpleDecoratedTextField(
                text = searchQuery,
                onValueChange = { attendanceViewModel.updateSearchQuery(it) },
                placeholder = "Search by firstname, ID or lastname...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.width(367.dp),
                clearButton = true
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allStudents.value, key = { it.studentId ?: 0 }) { student ->
                    val isSelected =
                        studentsInClass.any { it.studentId == student.studentId }

                    ClassStudentComposable(
                        modifier = Modifier.width(367.dp),
                        name = "${student.firstName} ${student.lastName}",
                        isSelected = isSelected,
                        onClickListener = {
                            classViewModel.toggleStudentSelection(
                                thisClass.classId,
                                student.studentId ?: return@ClassStudentComposable,
                                isSelected = !isSelected
                            )
                        }
                    )
                }
            }

            Button(
                text = "Save",
                symbol = Icons.Default.Check,
                color = SuccessColor,
                modifier = Modifier.width(367.dp),
                onClick = {
                    val updatedClass = thisClass.copy(
                        teacherName = teacherName,
                        type = classType,
                        startTime = startTime,
                        endTime = endTime
                    )
                    classViewModel.thisClass.value = updatedClass
                    classViewModel.updateClassState(thisClass.classId)
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }
    }
}
