package com.lra.kalanikethan.ui.screens.whosein

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.viewmodel.AttendanceViewModel
import com.lra.kalanikethan.ui.components.InfoBox
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentInfoCard
import kotlinx.coroutines.flow.map

@Composable
fun WhoseIn(
    attendanceViewModel: AttendanceViewModel,
    onEditFamily: (String) -> Unit = {}
) {
    val students by remember {
        attendanceViewModel.displayedStudents
            .map { it.filter { student -> student.signedIn } }
    }.collectAsState(emptyList())
    val searchQuery by attendanceViewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        attendanceViewModel.removeSearchQuery()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.width(1075.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleDecoratedTextField(
                text = searchQuery,
                onValueChange = { attendanceViewModel.updateSearchQuery(it) },
                placeholder = "Search by firstname, ID or lastname...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.width(875.dp),
                clearButton = true,
            )

            Spacer(modifier = Modifier.weight(1f))

            InfoBox(
                modifier = Modifier.height(30.dp).wrapContentWidth(),
                text = "Total Students: ${students.size}",
                fontSize = 16.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(students) { student ->
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
