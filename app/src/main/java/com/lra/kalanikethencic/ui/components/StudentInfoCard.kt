package com.lra.kalanikethencic.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.data.model.Student

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentInfoCard(
    studentData: Student,
    onSignInToggle: (studentId: Int) -> Unit,
    onAbsentClick: (studentId: Int) -> Unit,
    onEditClick: (studentId: Int) -> Unit,
) {

    val signedInText = if (studentData.signedIn) "Sign Out" else "Sign In"

    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp, // Figma blur roughly maps to elevation
                shape = RoundedCornerShape(0.dp), // or your desired shape
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .width(1075.dp)
            .wrapContentHeight()
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "${studentData.firstName} ${studentData.lastName}",
                style = MaterialTheme.typography.titleLarge,
            )

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

                Column(
                    modifier = Modifier
                        .wrapContentSize(),
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

                    Checkbox(
                        checked = studentData.canWalkAlone,
                        onCheckedChange = null,
                        interactionSource = remember { MutableInteractionSource() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            checkmarkColor = Color.White,
                            uncheckedColor = Color.LightGray,
                            disabledIndeterminateColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .size(30.dp),
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                )

                Column(
                    modifier = Modifier
                        .wrapContentSize(),
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

                    ClassBox(
                        text = studentData.birthdate.toString(),
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                )

                Column(
                    modifier = Modifier
                        .wrapContentSize(),
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
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (studentData.dance) {
                            ClassBox(
                                text = "Dance",
                            )
                        }
                        if (studentData.singing) {
                            ClassBox(
                                text = "Singing",
                            )
                        }
                        if (studentData.music) {
                            ClassBox(
                                text = "Music",
                            )
                        }
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                )


                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        text = "Edit Family",
                        symbol = Icons.Default.Edit,
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .wrapContentSize(),
                    )

                    Button(
                        text = "Sign Absent",
                        symbol = Icons.Default.Close,
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .wrapContentSize(),
                    )

                    Button(
                        text = signedInText,
                        symbol = Icons.Default.Check,
                        onClick = {
                            onSignInToggle(studentData.studentId)
                        },
                        modifier = Modifier
                            .wrapContentSize(),
                    )

                }





            }

        }


    }

}