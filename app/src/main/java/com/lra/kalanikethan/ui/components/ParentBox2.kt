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
fun ParentBox2(
    initialData: Parent,
    onFirstNameChange : (String) -> Unit = {},
    onLastNameChange : (String) -> Unit = {},
    onPhoneNumberChange : (String) -> Unit = {},
    deleteParent: () -> Unit = {},
) {
    var currentData by remember { mutableStateOf(initialData) }

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
                Button("Delete", Icons.Default.Delete, color = ErrorColor, onClick = { deleteParent() })

            }

            HorizontalDivider(color = Color.Gray, modifier = Modifier.fillMaxWidth())

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
                        errorMessage = emptyErrorMsg
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
                        errorMessage = emptyErrorMsg
                    )

                    SimpleDecoratedTextField(
                        placeholder = "Text",
                        text = currentData.phoneNumber.toString(),
                        onValueChange = {
                            currentData = currentData.copy(phoneNumber = it)
                            onPhoneNumberChange(it)
                        },
                        label = "Number",
                        error = false, //TODO("Check if phone number is actually optional")
                        errorMessage = emptyErrorMsg
                    )
                }
            }
        }
    }

