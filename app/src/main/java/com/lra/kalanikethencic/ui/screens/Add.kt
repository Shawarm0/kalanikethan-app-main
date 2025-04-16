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
        StudentBox(firstName= "Lovin", lastName = "Sharma", dance = true, sing = true, music = true)
        StudentBox(dance = true, sing = true)
    }
}