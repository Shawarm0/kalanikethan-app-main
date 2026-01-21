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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.StudentsViewModel
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentInfoCard
import kotlinx.coroutines.launch


/**
 * The sign in screen for the app.
 *
 * This screen displays a list of students and allows users to sign in or sign out.
 * It uses a [LazyColumn] to display the list of students and a [SimpleDecoratedTextField]
 * It uses [LaunchedEffect] to initialise the students realTime channel in the [SignInViewModel]
 *
 * @param viewModel The view model for the sign in screen.
 */
@Composable
fun SignIn(
    viewModel: StudentsViewModel
) {
    val students = viewModel.displayedStudents.collectAsState(emptyList())
    val searchQuery = viewModel.searchQuery.value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.initialiseStudentsChannel()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SimpleDecoratedTextField(
            text = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
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
                        viewModel.updateStudentAttendance(it, currentSignInStatus = !it.signedIn)
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