package com.lra.kalanikethan.ui.screens.DashBoardViewModel


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
import com.lra.kalanikethan.ui.components.StudentInfoCard
import com.lra.kalanikethan.ui.screens.SignIn.SignInViewModel


@Composable
fun Classes(
    viewModel: DashBoardViewModel,
    signInViewModel: SignInViewModel
) {
    val isLoading = viewModel.isLoading.value
    val thisClass = viewModel.thisClass.value
    val students = viewModel.getStudentsForClassFlow(thisClass.classId).collectAsState(emptyList())
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

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
            // Title Section with 113.dp padding from left
            Column(
                modifier = Modifier
                    .padding(start = 113.dp, top = 14.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "${thisClass.teacherName}'s Class",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        textWidth = with(density) { coordinates.size.width.toDp() }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.width(textWidth))
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Student List aligned with the title's start (113.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 113.dp)  // Same padding as title
                    .padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(students.value) { student ->
                    StudentInfoCard(
                        studentData = student,
                        onSignInToggle = {
                            signInViewModel.signIn(it)
                        },
                        onAbsentClick = { },
                        onEditClick = { }
                    )
                }
            }
        }
    }
}
