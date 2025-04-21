package com.lra.kalanikethencic.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lra.kalanikethencic.ui.components.StudentBox
import com.lra.kalanikethencic.ui.screens.SignIn.SignInViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Add(viewModel: SignInViewModel = hiltViewModel()) {
    val students = viewModel.filteredStudents.collectAsState(emptyList())
    Log.d("AddScreen", "Students count: ${students.value.size}")

    Column {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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
    }


}