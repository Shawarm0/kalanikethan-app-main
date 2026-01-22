package com.lra.kalanikethan.ui.screens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.StudentsViewModel
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.ui.components.InfoBox
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentInfoCard
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


/**
 * A composable screen that displays signed-in students with search functionality.
 *
 * This screen shows a list of students who are currently signed in, along with a search bar
 * to filter students by first name, ID, or last name.
 *
 *
 * @param viewModel The [SignInViewModel] that provides student data and handles business logic
 * for sign-in operations, search queries, and student management.
 *
 */
@Composable
fun WhoseIn(
    viewModel: StudentsViewModel
) {
    val students = viewModel.displayedStudents
        .map { studentList ->
            studentList.filter { student -> student.signedIn }
        }
        .collectAsState(emptyList())
    val searchQuery = viewModel.searchQuery.value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.initialiseStudentsChannel()
        viewModel.removeSearchQuery()
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
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = "Search by firstname, ID or lastname...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.width(875.dp),
                clearButton = true,
            )

            Spacer(modifier = Modifier.weight(1f))

            InfoBox(
                modifier = Modifier.height(30.dp).wrapContentWidth(),
                text = "Total Students: ${students.value.size}",
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
            items(students.value) { student ->
                StudentInfoCard(
                    studentData = student,
                    onSignInToggle = {
                        viewModel.updateStudentAttendance(it, currentSignInStatus = !it.signedIn, classId = null)
                    },
                    onAbsentClick = { },
                    onEditClick = {
                        coroutineScope.launch {
                            ChannelManager.unsubscribeFromAllChannels()
                        }
                    },
                )
            }
        }
    }
}