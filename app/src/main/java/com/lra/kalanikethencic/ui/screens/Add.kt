package com.lra.kalanikethencic.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lra.kalanikethencic.ui.components.StudentBox
import com.lra.kalanikethencic.ui.components.TabBar
import com.lra.kalanikethencic.ui.screens.SignIn.SignInViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Add(viewModel: SignInViewModel = hiltViewModel()) {
    val students = viewModel.filteredStudents.collectAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This will take all available space except what the TabBar needs
        LazyColumn(
            modifier = Modifier
                .weight(1f)  // Takes all remaining space
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(students.value) { student ->
                StudentBox(
                    initialData = student,
                    onConfirm = { updatedStudent ->
                        viewModel.updateStudent(updatedStudent)
                    }
                )
            }
        }

         // Spacer to push the TabBar to the bottom

        // TabBar will take its natural height at the bottom
        TabBar()
    }
}