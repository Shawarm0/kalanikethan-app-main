package com.lra.kalanikethencic.ui.components

import android.R
import android.R.attr.checked
import android.R.bool
import android.os.Build
import android.text.Editable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.theme.AccentColor
import com.lra.kalanikethencic.ui.theme.ButtonColor
import com.lra.kalanikethencic.ui.theme.ErrorColor
import com.lra.kalanikethencic.ui.theme.LightBoxBackground
import com.lra.kalanikethencic.ui.theme.PrimaryLightColor
import com.lra.kalanikethencic.ui.theme.SuccessColor
import com.lra.kalanikethencic.ui.theme.Typography
import com.lra.kalanikethencic.ui.theme.UnselectedButtonText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
data class StudentData(
    var firstName: String = "",
    var lastName: String = "",
    var birthday: String = LocalDate.of(1900, 1, 1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    var canWalkAlone: Boolean = false,
    var dance: Boolean = false,
    var sing: Boolean = false,
    var music: Boolean = false
)



@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentBox(
    initialData: StudentData = StudentData(),
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
                Text("Student", style = Typography.titleLarge)
                if(editableState){
                    Button("Delete", Icons.Default.Delete, color = ErrorColor)
                }
            }

            HorizontalDivider(color = Color.Gray, modifier = Modifier.fillMaxWidth())

            if(editableState){
                Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) { // Typing in data
                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.firstName,
                        onValueChange = {
                            tempData.firstName = it
                        },
                        label = "First Name",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.lastName,
                        onValueChange = {
                            tempData.lastName = it
                        },
                        label = "Last Name",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.birthday,
                        onValueChange = {
                            tempData.birthday = it
                        },
                        label = "Birthday",
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Classes: ", style = Typography.titleSmall)
                    SelectionButton("Dance", selected = remember { mutableStateOf(tempData.dance) }, onClick = {tempData.dance = it})
                    SelectionButton("Singing", selected = remember { mutableStateOf(tempData.sing) }, onClick = {tempData.sing = it})
                    SelectionButton("Music", selected = remember { mutableStateOf(tempData.music) }, onClick = {tempData.music = it})
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
                        Text("Can student walk alone?", style = Typography.titleSmall)
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
                            ButtonColor)
                        Button("Confirm", Icons.Default.Check,
                            onClick = {
                                currentData = tempData.copy()
                                editableState = false
                                //Saving data if edit is confirmed (idk what to put here)
                            },
                            SuccessColor
                        )
                    }
                }
            } else {
                Text("Student details", style = Typography.bodyMedium)
                Text("Name: ${currentData.firstName} ${currentData.lastName}", style = Typography.bodyMedium)
                Text("Birthdate: ${currentData.birthday}", style = Typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Text("Classes: ")
                    if (currentData.dance) {
                        ClassBox("Dance")
                    }
                    if (currentData.sing) {
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
                    Text("Can walk alone: ${if(currentData.canWalkAlone) "Yes" else "No"}", style = Typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        Button("View History", color = ButtonColor)
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
        .clip(RoundedCornerShape(20.dp))
        .background(color = Color(0xFFE7EEF5))
        .height(20.dp)
        .padding(horizontal = 8.dp)
        .wrapContentSize(Alignment.Center)
        ){
        Text(text = text, style = Typography.titleSmall, color = UnselectedButtonText)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun StudentInfo(){
    StudentBox()
}