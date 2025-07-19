package com.lra.kalanikethan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
/**
 * Displays a card UI with student information and interactive actions.
 *
 * @param studentData The [Student] object containing details to be displayed.
 * @param onSignInToggle Callback function invoked when the sign-in button is clicked.
 * @param onAbsentClick Callback function invoked when the "Sign Absent" button is clicked.
 * @param onEditClick Callback function invoked when the "Edit Family" button is clicked.
 */
fun StudentInfoCard(
    studentData: Student,
    onSignInToggle: (studentId: Int) -> Unit,
    onAbsentClick: (studentId: Int) -> Unit,
    onEditClick: (studentId: Int) -> Unit,
) {

    // Determine the label for the sign-in/sign-out button
    val signedInText = if (studentData.signedIn) "Sign Out" else "Sign In"

    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp, // Adds subtle elevation (similar to Figma blur)
                shape = RoundedCornerShape(0.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp)) // Rounds the card corners
            .background(Color.White) // Sets background to white
            .width(1075.dp) // Fixed width as per design spec
            .wrapContentHeight() // Height adjusts based on content
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // Student Full Name
            Text(
                text = "${studentData.firstName} ${studentData.lastName}",
                style = MaterialTheme.typography.titleLarge,
            )

            // Horizontal line to separate header
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                // Section: Can Leave Alone
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Can Leave Alone:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xff3d4d5c),
                            fontWeight = FontWeight.Normal,
                        ),
                    )

                    // Display a non-interactive checkbox for 'canWalkAlone' status
                    Checkbox(
                        checked = studentData.canWalkAlone,
                        onCheckedChange = null, // Disabled interaction
                        interactionSource = remember { MutableInteractionSource() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            checkmarkColor = Color.White,
                            uncheckedColor = Color.LightGray,
                            disabledIndeterminateColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.size(30.dp),
                    )
                }

                // Vertical divider between sections
                VerticalDivider(modifier = Modifier.height(50.dp))

                // Section: Birthdate
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Date of Birth:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xff3d4d5c),
                            fontWeight = FontWeight.Normal,
                        ),
                    )

                    // Custom composable to display date
                    ClassBox(text = studentData.birthdate.toString())
                }

                VerticalDivider(modifier = Modifier.height(50.dp))

                // Section: Classes enrolled
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Classes:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color(0xff3d4d5c),
                            fontWeight = FontWeight.Normal,
                        ),
                    )

                    Row(
                        modifier = Modifier.wrapContentHeight().wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Show only the classes the student is enrolled in
                        if (studentData.dance) {
                            ClassBox(text = "Dance")
                        }
                        if (studentData.singing) {
                            ClassBox(text = "Singing")
                        }
                        if (studentData.music) {
                            ClassBox(text = "Music")
                        }
                    }
                }

                VerticalDivider(modifier = Modifier.height(50.dp))

                // Section: Action Buttons
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Button to edit family info
                    Button(
                        text = "Edit Family",
                        symbol = Icons.Default.Edit,
                        onClick = {
                            onEditClick(studentData.studentId)
                        },
                        modifier = Modifier.wrapContentSize(),
                    )

                    // Button to mark student as absent
                    Button(
                        text = "Sign Absent",
                        symbol = Icons.Default.Close,
                        onClick = {
                            onAbsentClick(studentData.studentId)
                        },
                        modifier = Modifier.wrapContentSize(),
                    )

                    // Sign in / Sign out button
                    Button(
                        text = signedInText,
                        symbol = Icons.Default.Check,
                        onClick = {
                            onSignInToggle(studentData.studentId)
                        },
                        modifier = Modifier.wrapContentSize(),
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun StudentInfoCardPreview() {
    StudentInfoCard(
        studentData = Student(
            studentId = 1,
            familyId = 1,
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2000, 1, 1),
            canWalkAlone = true,
            dance = true,
            singing = false,
            music = true,
            signedIn = false,
        ),
        onSignInToggle = {},
        onAbsentClick = {},
        onEditClick = {}
    )
}