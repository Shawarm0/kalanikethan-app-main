package com.lra.kalanikethencic.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.components.Button
import com.lra.kalanikethencic.ui.components.Payment
import com.lra.kalanikethencic.ui.components.SelectionButton
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Payments(){
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Payment("Alberry", date = LocalDate.of(2025, 4, 13))
    }
}