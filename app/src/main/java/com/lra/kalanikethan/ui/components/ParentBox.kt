package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import com.lra.kalanikethan.ui.theme.SuccessColor

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ParentBox(
    initialData: Parent,
    onConfirm: (Parent) -> Unit = {},
    deleteParent: () -> Unit = {},
    viewHistory: Boolean = false,
    editable: Boolean = false,
    deleteIfCancelledOnFirstEdit: Boolean = false
) {
    // For the text boxes
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    var editableState by remember { mutableStateOf(editable) }

    val firstEdit = remember { mutableStateOf(true) }

    var currentData by remember { mutableStateOf(initialData) }
    var tempData by remember { mutableStateOf(initialData.copy()) }

    val firstNameError = remember { mutableStateOf(false) }
    val lastNameError = remember { mutableStateOf(false) }
    val numberError = remember { mutableStateOf(false) }

    val emptyErrorMsg = "Cannot be empty"

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
                Text("Parent Details", style = MaterialTheme.typography.titleLarge)
                if(editableState){
                    Button("Delete", Icons.Default.Delete, color = ErrorColor, onClick = { deleteParent() })
                }
            }

            HorizontalDivider(color = Color.Gray, modifier = Modifier.fillMaxWidth())

            if(editableState) {
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
                        errorMessage = emptyErrorMsg
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
                        errorMessage = emptyErrorMsg
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = tempData.phoneNumber.toString(),
                        onValueChange = {
                            tempData = tempData.copy(phoneNumber = it)
                        },
                        label = "Number",
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = numberError.value,
                        errorMessage = emptyErrorMsg
                    )



                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) { //Bottom Row

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            "Cancel",
                            Icons.Default.Clear,
                            onClick = {
                                if(firstEdit.value && deleteIfCancelledOnFirstEdit){
                                    deleteParent()
                                } else {
                                    tempData = currentData.copy()
                                    editableState = false
                                }
                            },
                            color = ButtonColor
                        )
                        Button(
                            "Confirm", Icons.Default.Check,
                            onClick = {
                                if(tempData.firstName == ""){
                                    firstNameError.value = true
                                } else {
                                    firstNameError.value = false
                                }

                                if(tempData.lastName == ""){
                                    lastNameError.value = true
                                } else {
                                    lastNameError.value = false
                                }

                                if(tempData.phoneNumber == ""){
                                    numberError.value = true
                                } else {
                                    numberError.value = false
                                }

                                if(!firstNameError.value && !lastNameError.value && !numberError.value){
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
                Text("Number: ${currentData.phoneNumber}", style = MaterialTheme.typography.bodyMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                        Spacer(modifier = Modifier.weight(1f))
                        Button("Edit Details", color = ButtonColor, onClick = {editableState = true})
                    }
                }

            }
        }
    }

}