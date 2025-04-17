package com.lra.kalanikethencic.ui.screens.SignIn

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lra.kalanikethencic.ui.components.StudentInfoCard

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignIn(viewModel: SignInViewModel = hiltViewModel()) {
    // Accessing the students list via .value
    val students = viewModel.students.value

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(students.size) { index ->
            StudentInfoCard(
                studentData = students[index],
                onSignInToggle = { studentId ->
                    viewModel.toggleSignedIn(studentId)
                },
                onAbsentClick = { },
                onEditClick = { },
            )
        }
    }
}
