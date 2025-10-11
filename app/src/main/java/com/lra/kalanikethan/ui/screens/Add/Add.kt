package com.lra.kalanikethan.ui.screens.Add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.ParentBox
import com.lra.kalanikethan.ui.components.ParentBox2
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentBox
import com.lra.kalanikethan.ui.components.StudentBox2
import com.lra.kalanikethan.ui.components.Tab
import com.lra.kalanikethan.ui.components.TabBar
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

data class PaymentData(
    val paymentId: String = "",
    val email: String = "",
    val paymentDate: String = "",
    val familyName: String = "",
    val amount: String = ""
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Add(addViewModel: AddViewModel) {
    val students by addViewModel.students.collectAsState(emptyList())
    val parents by addViewModel.parents.collectAsState(emptyList())
    val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() }
    // Single state object for payment data
    var paymentData by remember { mutableStateOf(PaymentData()) }

    val dateError = remember { mutableStateOf(false) }
    val invalid = remember {mutableStateOf(false)}

    Column(modifier = Modifier.padding(start = 106.dp, top = 14.dp, bottom = 14.dp, end = 106.dp)) {
        LazyColumn() {
            stickyHeader {
                Box(Modifier.background(Background).fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Students",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Button(
                            text = "Add student",
                            onClick = {
                                addViewModel.createStudent()
                            }
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
            items(students, key = {student -> student.internalID!! }){ student ->
                StudentBox2(
                    initialData = student,
                    onFirstNameChange = {
                        val updatedStudent = student.copy(firstName = it)
                        val index = students.indexOf(student)

                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    onLastNameChange = {
                        val updatedStudent = student.copy(lastName = it)
                        val index = students.indexOf(student)
                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    onBirthDateChange = {
                        val date : LocalDate
                        try {
                            date = LocalDate.parse(it, formatter)
                            val updatedStudent = student.copy(birthdate = date)
                            val index = students.indexOf(student)
                            dateError.value = false
                            addViewModel.updateStudent(index, updatedStudent)
                        } catch (e : Exception){
                            dateError.value = true
                        }
                    },
                    onDanceChange = {
                        val updatedStudent = student.copy(dance = it)
                        val index = students.indexOf(student)
                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    onSingingChange = {
                        val updatedStudent = student.copy(singing = it)
                        val index = students.indexOf(student)
                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    onMusicChange = {
                        val updatedStudent = student.copy(music = it)
                        val index = students.indexOf(student)
                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    onWalkAloneChange = {
                        val updatedStudent = student.copy(canWalkAlone = it)
                        val index = students.indexOf(student)
                        addViewModel.updateStudent(index, updatedStudent)
                    },
                    deleteStudent = {
                        addViewModel.deleteStudent(student)
                    },
                    index = student.internalID,
                    dateInvalid = dateError.value
                )
                Spacer(Modifier.height(14.dp))
            }
            stickyHeader {
                Box(Modifier.background(Background).fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Parents",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Button(
                            text = "Add parent",
                            onClick = {
                                addViewModel.createParent()
                            }
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
            items(parents, key = { it.internalID!! }){ parent ->
                ParentBox2(
                    initialData = parent,
                    onFirstNameChange = {
                        val updatedParent = parent.copy(firstName = it)
                        val index = parents.indexOf(parent)
                        addViewModel.updateParent(index, updatedParent)
                    },
                    onLastNameChange = {
                        val updatedParent = parent.copy(lastName = it)
                        val index = parents.indexOf(parent)
                        addViewModel.updateParent(index, updatedParent)
                    },
                    onPhoneNumberChange = {
                        val updatedParent = parent.copy(phoneNumber = it)
                        val index = parents.indexOf(parent)
                        addViewModel.updateParent(index, updatedParent)
                    },
                    deleteParent = {
                        addViewModel.deleteParent(parent)
                    }
                )
                Spacer(Modifier.height(14.dp))
            }
            stickyHeader {
                Box(Modifier.background(Background).fillMaxWidth()) {
                    Row {
                        Text(
                            "Details",
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Enter the details below to proceed.",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                ),
                                color = UnselectedButtonText,
                            )

                        }


                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.spacedBy(13.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(25.dp)
                            ) {

                                SimpleDecoratedTextField(
                                    text = paymentData.familyName,
                                    placeholder = "Enter Family Name",
                                    label = "Family Name",
                                    onValueChange = {
                                        paymentData = paymentData.copy(familyName = it)
                                    },
                                    error = paymentData.familyName.isBlank(),
                                    errorMessage = "Cannot be empty"
                                )

                                SimpleDecoratedTextField(
                                    text = paymentData.paymentId,
                                    placeholder = "Enter Payment ID",
                                    label = "Payment ID",
                                    onValueChange = {
                                        paymentData = paymentData.copy(paymentId = it)
                                    },
                                    error = paymentData.paymentId.isBlank(),
                                    errorMessage = "Cannot be empty"
                                )

                                SimpleDecoratedTextField(
                                    text = paymentData.email,
                                    placeholder = "Enter Email",
                                    label = "Email",
                                    onValueChange = {
                                        paymentData = paymentData.copy(email = it)
                                    },
                                    error = paymentData.email.isBlank(),
                                    errorMessage = "Cannot be empty"
                                )
                            }


                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(25.dp)
                            ) {

                                SimpleDecoratedTextField(
                                    text = paymentData.paymentDate,
                                    placeholder = "DD/MM/YYYY",
                                    label = "Enter Payment Date",
                                    isLeadingIconClickable = true,
                                    leadingIcon = Icons.Default.CalendarMonth,
                                    onValueChange = {
                                        if (it.isBlank()){
                                            dateError.value = true
                                        }
                                        try {
                                            LocalDate.parse(it, formatter)
                                            paymentData = paymentData.copy(paymentDate = it)
                                            dateError.value = false
                                        } catch (e : Exception){
                                            dateError.value = true
                                        }
                                    },
                                    error = dateError.value,
                                    errorMessage = "Invalid Data (DD/MM/YYYY)"
                                )

                                SimpleDecoratedTextField(
                                    text = paymentData.amount,
                                    placeholder = "Enter Amount",
                                    label = "Amount",
                                    onValueChange = {
                                        paymentData = paymentData.copy(amount = it)
                                    },
                                    error = paymentData.amount.isBlank(),
                                    errorMessage = "Cannot be empty",
                                    floatsOnly = true
                                )
                            }
                        }


                        Button(
                            text = "Create Family",
                            symbol = Icons.Default.Check,
                            onClick = {
                                if(checkError(students, parents, paymentData, dateError.value)){
                                    addViewModel.createFamily(paymentData, parents, students)
                                } else {
                                    invalid.value = true
                                }
                            },
                            color = SuccessColor
                        )
                        if(invalid.value){
                            Text("Couldn't create family, some values are missing or invalid", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

fun checkError(students : List<Student>, parents : List<Parent>, paymentData: PaymentData, dateError : Boolean) : Boolean{
    if (dateError) return false
    for (student in students){
        if(student.firstName.isBlank()){
            return false
        }
        if(student.firstName.isBlank()){
            return false
        }
    }
    for(parent in parents){
        if(parent.firstName.isBlank()){
            return false
        }
        if(parent.lastName.isBlank()){
            return false
        }
    }
    if (paymentData.familyName.isBlank()) return false
    if (paymentData.paymentId.isBlank()) return false
    if (paymentData.amount.isBlank()) return false
    if (paymentData.email.isBlank()) return false

    return true
}