package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.PrimaryLightColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    // DatePickerDialog with confirm and dismiss buttons
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK", color = Color.Black)
            }
        },
        colors = DatePickerDefaults.colors( // This is the important part
            containerColor = Background
        ),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel",  color = Color.Black)
            }
        },
    )
    {
        Surface( // <- sets the background for the whole dialog, including buttons
            color = Background
        ) {
            DatePicker(
                modifier = Modifier.background(Background),
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Background,
                    titleContentColor = Color.Black,
                    weekdayContentColor = Color.Black,
                    dayContentColor = Color.Black,
                    selectedYearContentColor = Color.White,
                    currentYearContentColor = Color.Black,
                    todayDateBorderColor = PrimaryLightColor,
                    todayContentColor = Color.Black,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = PrimaryLightColor,
                    selectedYearContainerColor = PrimaryLightColor,
                )
            )
        }
    }
}