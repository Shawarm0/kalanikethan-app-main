package com.lra.kalanikethan.ui.components

import android.util.Log
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import java.time.format.DateTimeFormatter

private class EmptyFirstName : Exception("First name text field is empty")
private class EmptyLastName : Exception("Last name text field is empty")
private class InvalidDate : Exception("Date value is invalid and cannot be parsed")
@OptIn(ExperimentalFoundationApi::class)
@Composable
/**
 * This is the studentbox Composable that holds the student data
 *
 * @param initialData the initial data of the student in the form of a [Student] data object
 * @param onConfirm a lambda that is called when the confirm button is clicked
 * @param editable a boolean that determines if the studentbox is editable or not
 * @param deleteIfCancelledOnFirstEdit if a student box is being edited for the first time and is cancelled, if this is true the student will instead be deleted
 */
fun StudentBox(
    initialData: Student,
    onConfirm: (Student) -> Unit = {},
    deleteStudent: () -> Unit = {},
    viewHistory: Boolean = false,
    editable: Boolean = false,
    deleteIfCancelledOnFirstEdit: Boolean = false
){
    // For the text boxes
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    var editableState by remember { mutableStateOf(editable) }

    val firstEdit = remember { mutableStateOf(true) }
    var currentData by remember { mutableStateOf(initialData) }
    var tempData by remember { mutableStateOf(initialData.copy()) }

    val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() }

    val birthdate = remember {mutableStateOf(currentData.birthdate.format(formatter))}

    val dateError = remember { mutableStateOf(false) }
    val firstNameError = remember { mutableStateOf(false) }
    val lastNameError = remember { mutableStateOf(false) }

    val dateErrorMsg = remember { mutableStateOf("") }
    val firstNameErrorMsg = remember { mutableStateOf("") }
    val lastNameErrorMsg = remember { mutableStateOf("") }


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
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = firstNameError.value,
                        errorMessage = firstNameErrorMsg.value
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.lastName,
                        onValueChange = {
                            tempData = tempData.copy(lastName = it)
                        },
                        label = "Last Name",
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = lastNameError.value,
                        errorMessage = lastNameErrorMsg.value
                    )

                    SimpleDecoratedTextField(
                        placeholder = "DD/MM/YYYY",
                        text = if (tempData.birthdate.toString() == "2000-01-01") "" else tempData.birthdate.format(formatter),
                        onValueChange = {
                            birthdate.value = it
                        },
                        isLeadingIconClickable = true,
                        leadingIcon = Icons.Default.CalendarMonth,
                        label = "Birthday",
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = dateError.value,
                        errorMessage = dateErrorMsg.value
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
                                if(firstEdit.value && deleteIfCancelledOnFirstEdit){
                                    deleteStudent()
                                } else {
                                    tempData = currentData.copy()
                                    editableState = false
                                }
                            },
                            color = ButtonColor
                        )
                        Button("Confirm", Icons.Default.Check,
                            onClick = {

                                try{
                                    tempData = tempData.copy(birthdate = LocalDate.parse(birthdate.value, formatter))
                                    dateError.value = false
                                } catch (e : Exception){
                                    Log.e("Add Family", "Invalid date : $e")
                                    dateError.value = true
                                    dateErrorMsg.value = "Invalid date (DD/MM/YYYY)"
                                }

                                try {
                                    if (tempData.firstName.isEmpty()) throw EmptyFirstName()
                                    firstNameError.value = false
                                } catch (e : EmptyFirstName){
                                    Log.e("Add Family", "First name text field is empty")
                                    firstNameError.value = true
                                    firstNameErrorMsg.value = "Cannot be left empty"
                                }

                                try {
                                    if (tempData.lastName.isEmpty()) throw EmptyLastName()
                                    lastNameError.value = false
                                } catch (e : EmptyLastName){
                                    Log.e("Add Family", "Last name text field is empty")
                                    lastNameError.value = true
                                    lastNameErrorMsg.value = "Cannot be left empty"
                                }

                                if(!dateError.value && !firstNameError.value && !lastNameError.value){
                                    firstEdit.value = false
                                    currentData = tempData.copy()
                                    editableState = false
                                    onConfirm(tempData)
                                }
                            },
                            color = SuccessColor
                        )
                    }
                }
            } else {
                Text("Name: ${currentData.firstName} ${currentData.lastName}", style = MaterialTheme.typography.bodyMedium)
                Text("Birthdate: ${currentData.birthdate.format(formatter)}", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Text("Classes: ")
                    if (currentData.dance) {
                        InfoBox(text = "Dance")
                    }
                    if (currentData.singing) {
                        InfoBox(text = "Singing")
                    }
                    if (currentData.music) {
                        InfoBox(text = "Music")
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
fun InfoBox(
    modifier: Modifier = Modifier.height(30.dp).wrapContentWidth(),
    text: String,
    fontSize: TextUnit = 14.sp,
){
    Box(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(color = Color(0xFFE7EEF5))
        .height(30.dp)
        .padding(horizontal = 10.dp)
        .wrapContentWidth(),
        contentAlignment = Alignment.Center
        ){
        Text(text = text, style = MaterialTheme.typography.titleSmall.copy(
            fontSize = fontSize
        ), color = UnselectedButtonText)
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