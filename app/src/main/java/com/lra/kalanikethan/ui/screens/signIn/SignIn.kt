package com.lra.kalanikethan.ui.screens.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentInfoCard

@Composable
fun SignIn(
    attendanceViewModel: AttendanceViewModel,
    onEditFamily: (String) -> Unit = {}
) {
    val students = attendanceViewModel.displayedStudents.collectAsState(emptyList())
    val searchQuery by attendanceViewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SimpleDecoratedTextField(
            text = searchQuery,
            onValueChange = { attendanceViewModel.updateSearchQuery(it) },
            placeholder = "Search by firstname, ID or lastname...",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier.width(1075.dp),
            clearButton = true,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(students.value) { student ->
                StudentInfoCard(
                    studentData = student,
                    onSignInToggle = {
                        attendanceViewModel.updateStudentAttendance(it, currentSignInStatus = !it.signedIn, classId = null)
                    },
                    onAbsentClick = { },
                    onEditClick = { familyId -> onEditFamily(familyId) },
                )
            }
        }
    }
}
