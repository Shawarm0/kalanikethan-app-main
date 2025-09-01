package com.lra.kalanikethan.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import com.lra.kalanikethan.util.convertLongToTime

@Composable
fun HistoryComponent(
    modifier: Modifier = Modifier
        .width(1075.dp)
        .wrapContentHeight()
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .clip(RoundedCornerShape(12.dp))
        .background(Color.White),
    day: String,
    data: List<History>,
    studentMap: Map<Int?, Student>,
    employeeMap: Map<String, Employee>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .clickable(
                    onClick = { isExpanded = !isExpanded },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = PrimaryLightColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (isExpanded) 180f else 0f),
                    tint = Color.Black
                )
            }

            // Expandable Events
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    for (event in data) {
                        // Calculate student name for each event
                        val studentName = event.studentID?.let { studentId ->
                            studentMap[studentId]?.let { student ->
                                "${student.firstName} ${student.lastName}".trim()
                            }
                        } ?: "Unknown Student"

                        // Calculate employee name for each event
                        val employeeName = employeeMap[event.uid]?.let { employee ->
                            "${employee.firstName} ${employee.lastName}".trim()
                        } ?: "Unknown Employee"

                        EventComponent(
                            data = event,
                            studentName = studentName, // Pass the calculated student name,
                            employeeName = employeeName
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventComponent(
    data: History,
    studentName: String,
    employeeName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Name Column - Fixed width
        Column(
            modifier = Modifier.width(200.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Name:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = studentName,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Signed In Column - Fixed width
        Column(
            modifier = Modifier.width(150.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Signed In:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = convertLongToTime(data.signInTime) ?: "Not Signed In",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        // Signed Out Column - Fixed width
        Column(
            modifier = Modifier.width(150.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Signed Out:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = convertLongToTime(data.signOutTime) ?: "Not signed out",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        // Employee Name Column - Fixed width
        Column(
            modifier = Modifier.width(200.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Employee Name:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = employeeName,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}