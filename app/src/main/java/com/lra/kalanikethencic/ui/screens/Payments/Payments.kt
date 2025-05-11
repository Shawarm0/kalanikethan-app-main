package com.lra.kalanikethencic.ui.screens.Payments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.components.Payment
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Payments(){
    Column(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Payment("Alberry", date = LocalDate.of(2025, 4, 13), price = 13f)
        Payment("Duong", date = LocalDate.of(2025, 5, 9), price = 10f, id = "RD905")
        Payment("Sharma", date = LocalDate.of(2025, 4, 29), price = 8f, id = "LS420")

    }
}