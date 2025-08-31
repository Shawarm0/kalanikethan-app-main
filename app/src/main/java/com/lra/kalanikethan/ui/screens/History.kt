package com.lra.kalanikethan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.ui.components.HistoryComponent

@Composable
fun History() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val testHistoryList = listOf(
            History(
                historyID = 1,
                studentID = 123,
                classID = 456,
                date = "2024-01-15",
                signInTime = 1705312800000, // January 15, 2024 10:00:00 AM
                signOutTime = 1705323600000, // January 15, 2024 1:00:00 PM
                uid = "uid_123_1705312800000"
            ),
            History(
                historyID = 2,
                studentID = 123,
                classID = 456,
                date = "2024-01-16",
                signInTime = 1705399200000, // January 16, 2024 10:00:00 AM
                signOutTime = 1705410000000, // January 16, 2024 1:00:00 PM
                uid = "uid_123_1705399200000"
            ),
            History(
                historyID = 3,
                studentID = 123,
                classID = 789,
                date = "2024-01-17",
                signInTime = 1705485600000, // January 17, 2024 10:00:00 AM
                signOutTime = null, // Still signed in
                uid = "uid_123_1705485600000"
            ),
            History(
                historyID = 4,
                studentID = 456,
                classID = 456,
                date = "2024-01-15",
                signInTime = 1705316400000, // January 15, 2024 11:00:00 AM
                signOutTime = 1705327200000, // January 15, 2024 2:00:00 PM
                uid = "uid_456_1705316400000"
            ),
            History(
                historyID = 1,
                studentID = 123,
                classID = 456,
                date = "2024-01-15",
                signInTime = 1705312800000, // January 15, 2024 10:00:00 AM
                signOutTime = 1705323600000, // January 15, 2024 1:00:00 PM
                uid = "uid_123_1705312800000"
            ),
            History(
                historyID = 2,
                studentID = 123,
                classID = 456,
                date = "2024-01-16",
                signInTime = 1705399200000, // January 16, 2024 10:00:00 AM
                signOutTime = 1705410000000, // January 16, 2024 1:00:00 PM
                uid = "uid_123_1705399200000"
            ),
            History(
                historyID = 3,
                studentID = 123,
                classID = 789,
                date = "2024-01-17",
                signInTime = 1705485600000, // January 17, 2024 10:00:00 AM
                signOutTime = null, // Still signed in
                uid = "uid_123_1705485600000"
            ),
            History(
                historyID = 4,
                studentID = 456,
                classID = 456,
                date = "2024-01-15",
                signInTime = 1705316400000, // January 15, 2024 11:00:00 AM
                signOutTime = 1705327200000, // January 15, 2024 2:00:00 PM
                uid = "uid_456_1705316400000"
            )
        )
        HistoryComponent(
            day = "Monday",
            data = testHistoryList
        )
    }
}

