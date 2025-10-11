package com.lra.kalanikethan.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.SuccessColor
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun StudentBox2(
    initialData: Student,
    onFirstNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onBirthDateChange: (String) -> Unit = {},
    onDanceChange: (Boolean) -> Unit = {},
    onSingingChange: (Boolean) -> Unit = {},
    onMusicChange: (Boolean) -> Unit = {},
    onWalkAloneChange: (Boolean) -> Unit = {},
    deleteStudent: () -> Unit = {},
    dateInvalid : Boolean = false,
    index : Int? = null
){
    var currentData by remember { mutableStateOf(initialData) }

    val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() }

    val birthdate = remember {mutableStateOf(currentData.birthdate.format(formatter))}

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
                Button("Delete", Icons.Default.Delete, color = ErrorColor, onClick = { deleteStudent() })
            }

            HorizontalDivider(color = Color.Gray, modifier = Modifier.fillMaxWidth())
            Text("Local ID: $index")


                Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) { // Typing in data

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = currentData.firstName,
                        onValueChange = {
                            currentData = currentData.copy(firstName = it)
                            onFirstNameChange(it)
                        },
                        label = "First Name",
                        error = currentData.firstName.isBlank(),
                        errorMessage = firstNameErrorMsg.value
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = currentData.lastName,
                        onValueChange = {
                            currentData = currentData.copy(lastName = it)
                            onLastNameChange(it)
                        },
                        label = "Last Name",
                        error = currentData.lastName.isBlank(),
                        errorMessage = lastNameErrorMsg.value
                    )

                    SimpleDecoratedTextField(
                        placeholder = "DD/MM/YYYY",
                        text = if (currentData.birthdate.toString() == "2000-01-01") "" else currentData.birthdate.format(formatter),
                        onValueChange = {
                            birthdate.value = it
                            onBirthDateChange(it)
                        },
                        isLeadingIconClickable = true,
                        leadingIcon = Icons.Default.CalendarMonth,
                        label = "Birthday",
                        error = dateInvalid,
                        errorMessage = dateErrorMsg.value
                    )
                }



                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Classes: ", style = MaterialTheme.typography.titleSmall)
                    SelectionButton(
                        "Dance",
                        selected = remember { mutableStateOf(currentData.dance) },
                        onClick = {
                            currentData = currentData.copy(dance = it)
                            onDanceChange(it)
                        })
                    SelectionButton(
                        "Singing",
                        selected = remember { mutableStateOf(currentData.singing) },
                        onClick = {
                            currentData = currentData.copy(singing = it)
                            onSingingChange(it)
                        })
                    SelectionButton(
                        "Music",
                        selected = remember { mutableStateOf(currentData.music) },
                        onClick = {
                            currentData = currentData.copy(music = it)
                            onMusicChange(it)
                        })
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
                            checked = currentData.canWalkAlone,
                            onCheckedChange = {
                                currentData = currentData.copy(canWalkAlone = it)
                                onWalkAloneChange(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = PrimaryLightColor,
                                uncheckedThumbColor = Color(0xFF79747E),
                                uncheckedTrackColor = Color(0xFFe7e0ec),
                                uncheckedBorderColor = Color(0xFF79747E)
                            ),
                            thumbContent = if (currentData.canWalkAlone) {
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
                }
        }
    }
}