package com.lra.kalanikethan.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.viewmodel.ClassManagementViewModel
import com.lra.kalanikethan.ui.components.StudentInfoCard
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Classes(
    classViewModel: ClassManagementViewModel,
    attendanceViewModel: AttendanceViewModel,
    onEditFamily: (String) -> Unit = {}
) {
    val isLoading by classViewModel.isLoading.collectAsState()
    val thisClass by classViewModel.thisClass.collectAsState()
    val students by remember(thisClass?.classId) {
        thisClass?.classId?.let { classViewModel.studentsForClass(it) }
            ?: MutableStateFlow(emptyList())
    }.collectAsState()

    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    LaunchedEffect(thisClass?.classId) {
        thisClass?.classId?.let { classId ->
            classViewModel.loadStudentsForClass(classId)
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 113.dp, top = 14.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "${thisClass?.teacherName}'s Class",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        textWidth = with(density) { coordinates.size.width.toDp() }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.width(textWidth))
                Spacer(modifier = Modifier.height(14.dp))
            }

            val sortedStudents = remember(students) { students.sortedBy { it.firstName } }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 113.dp)
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sortedStudents, key = { it.studentId ?: 0 }) { student ->
                    StudentInfoCard(
                        studentData = student,
                        onSignInToggle = {
                            attendanceViewModel.updateStudentAttendance(it, currentSignInStatus = !it.signedIn, classId = thisClass?.classId)
                        },
                        onAbsentClick = { },
                        onEditClick = { familyId -> onEditFamily(familyId) }
                    )
                }
            }
        }
    }
}
