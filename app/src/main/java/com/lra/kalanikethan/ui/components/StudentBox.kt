package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate



@OptIn(ExperimentalFoundationApi::class)
@Composable
/**
 * This is the studentbox Composable that holds the student data
 *
 * @param initialData the initial data of the student in the form of a [Student] data object
 * @param onConfirm a lambda that is called when the confirm button is clicked
 * @param editable a boolean that determines if the studentbox is editable or not
 */
fun StudentBox(
    initialData: Student,
    onConfirm: (Student) -> Unit = {},
    deleteStudent: () -> Unit = {},
    viewHistory: Boolean = false,
    editable: Boolean = false
){
    // For the text boxes
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    var editableState by remember { mutableStateOf(editable) }

    var currentData by remember { mutableStateOf(initialData) }
    var tempData by remember { mutableStateOf(initialData.copy()) }



    Box(modifier = Modifier
        .shadow(
            elevation = 4.dp, // Figma blur roughly maps to elevation
            shape = RoundedCornerShape(0.dp), // or your desired shape
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .width(1075.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(color = LightBoxBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(//Title + Delete button (if editable)
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Student Details", style = MaterialTheme.typography.titleLarge)
                if(editableState){
                    Button("Delete", Icons.Default.Delete, color = ErrorColor, onClick = { deleteStudent() })
                }
            }

            HorizontalDivider(color = Color.Gray, modifier = Modifier.fillMaxWidth())

            if(editableState){
                Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) { // Typing in data

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.firstName,
                        onValueChange = {
                            tempData = tempData.copy(firstName = it)
                        },
                        label = "First Name",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.lastName,
                        onValueChange = {
                            tempData = tempData.copy(lastName = it)
                        },
                        label = "Last Name",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.birthdate.toString(),
                        onValueChange = {
                            // Figure out how to change date
                        },
                        label = "Birthday",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Classes: ", style = MaterialTheme.typography.titleSmall)
                    SelectionButton(
                        "Dance",
                        selected = remember { mutableStateOf(tempData.dance) },
                        onClick = { tempData = tempData.copy(dance = it) })
                    SelectionButton(
                        "Singing",
                        selected = remember { mutableStateOf(tempData.singing) },
                        onClick = { tempData = tempData.copy(singing = it) })
                    SelectionButton(
                        "Music",
                        selected = remember { mutableStateOf(tempData.music) },
                        onClick = { tempData = tempData.copy(music = it)})
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){ //Bottom Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){ //Walk Alone switch
                        Text("Can student walk alone?", style = MaterialTheme.typography.titleSmall)
                        Switch(
                            checked = tempData.canWalkAlone,
                            onCheckedChange = {
                                tempData = tempData.copy(canWalkAlone = it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PrimaryLightColor,
                                uncheckedThumbColor = Color(0xFF79747E),
                                uncheckedTrackColor = Color(0xFFe7e0ec),
                                uncheckedBorderColor = Color(0xFF79747E)
                            ),
                            thumbContent = if (tempData.canWalkAlone) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button("Cancel",
                            Icons.Default.Clear,
                            onClick = {
                                tempData = currentData.copy()
                                editableState = false
                                      },
                            color = ButtonColor
                        )
                        Button("Confirm", Icons.Default.Check,
                            onClick = {
                                currentData = tempData.copy()
                                editableState = false
                                onConfirm(tempData)
                            },
                            color = SuccessColor
                        )
                    }
                }
            } else {
                Text("Name: ${currentData.firstName} ${currentData.lastName}", style = MaterialTheme.typography.bodyMedium)
                Text("Birthdate: ${currentData.birthdate}", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Text("Classes: ")
                    if (currentData.dance) {
                        ClassBox("Dance")
                    }
                    if (currentData.singing) {
                        ClassBox("Singing")
                    }
                    if (currentData.music) {
                        ClassBox("Music")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Can walk alone: ${if(currentData.canWalkAlone) "Yes" else "No"}", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        if (viewHistory) {
                            Button("View History", color = ButtonColor)
                        }
                        Button("Edit Details", color = ButtonColor, onClick = {editableState = true})
                    }
                }

            }
        }
    }
}

@Composable
fun ClassBox(text: String){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(color = Color(0xFFE7EEF5))
        .height(30.dp)
        .padding(horizontal = 10.dp)
        .wrapContentWidth(),
        contentAlignment = Alignment.Center
        ){
        Text(text = text, style = MaterialTheme.typography.titleSmall, color = UnselectedButtonText)
    }
}

@Preview
@Composable
fun StudentBoxPreview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StudentBox(
            initialData = Student(
                studentId = 1,
                familyId = "",
                firstName = "John",
                lastName = "Doe",
                birthdate = TODO(),
                canWalkAlone = true,
                dance = true,
                singing = false,
                music = true,
                signedIn = false,
            )
        )
    }
}