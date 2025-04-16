package com.lra.kalanikethencic.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.components.Payment
import com.lra.kalanikethencic.ui.components.StudentBox
import com.lra.kalanikethencic.ui.components.StudentData
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Add(){
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
        .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ){
        StudentBox(
            initialData = StudentData(
                firstName = "Lovin",
                lastName = "Sharma",
                birthday = LocalDate.of(1900, 1, 1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                canWalkAlone = false,
                dance = true,
                sing = true,
            )
        )
        StudentBox(
            initialData = StudentData(
                firstName = "Ahmed",
                lastName = "Alberry",
                birthday = LocalDate.of(1900, 1, 1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                canWalkAlone = true,
                music = true,
            )
        )

    }
}