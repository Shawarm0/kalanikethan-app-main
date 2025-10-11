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
    val parents = remember { mutableStateListOf<Parent>() }
    val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() }
    // Single state object for payment data
    var paymentData by remember { mutableStateOf(PaymentData()) }

    Column(modifier = Modifier.padding(start = 106.dp, top = 14.dp, bottom = 14.dp)) {

        LazyColumn() {
            stickyHeader {
                Box(Modifier.background(Background).fillMaxWidth()) {
                    Row {
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
                val dateError = remember { mutableStateOf(false) }
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
            }
            stickyHeader {
                Box(Modifier.background(Background)) {
                    Row {
                        Text(
                            "Parents",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Button(
                            text = "Add parent",
                            onClick = {

                            }
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
            stickyHeader {
                Box(Modifier.background(Background)) {
                    Row {
                        Text(
                            "Details",
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PaymentContent(
    paymentData: PaymentData,
    onPaymentDataChanged: (PaymentData) -> Unit,
    createFamily: () -> Unit,
) {
    // For the text boxes
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val nameError = remember { mutableStateOf(false) }
    val idError = remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf(false) }
    val dateError = remember { mutableStateOf(false) }
    val amountError = remember { mutableStateOf(false) }

    val emptyErrorMsg = "Cannot be empty"
    val invalidDateMsg = "Invalid date (DD/MM/YYYY)"
    val amountTypeMsg = "Input a number"


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
                    text = "Payment Details",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 40.sp,
                        lineHeight = 40.sp
                    ),
                    color = Color.Black
                )

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
                            text = paymentData.paymentId,
                            placeholder = "Enter Family Name",
                            label = "Family Name",
                            onValueChange = {
                                onPaymentDataChanged(paymentData.copy(familyName = it))
                            },
//                            bringIntoViewRequester = bringIntoViewRequester,
//                            coroutineScope = coroutineScope,
                            error = nameError.value,
                            errorMessage = emptyErrorMsg
                        )

                    SimpleDecoratedTextField(
                        text = paymentData.paymentId,
                        placeholder = "Enter Payment ID",
                        label = "Payment ID",
                        onValueChange = {
                            onPaymentDataChanged(paymentData.copy(paymentId = it))
                        },
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = idError.value,
                        errorMessage = emptyErrorMsg
                    )

                    SimpleDecoratedTextField(
                        text = paymentData.email,
                        placeholder = "Enter Email",
                        label = "Email",
                        onValueChange = {
                            onPaymentDataChanged(paymentData.copy(email = it))
                        },
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = emailError.value,
                        errorMessage = emptyErrorMsg
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
                            onPaymentDataChanged(paymentData.copy(paymentDate = it))
                        },
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = dateError.value,
                        errorMessage = invalidDateMsg
                    )

                    SimpleDecoratedTextField(
                        text = paymentData.amount,
                        placeholder = "Enter Amount",
                        label = "Amount",
                        onValueChange = {
                            onPaymentDataChanged(paymentData.copy(amount = it))
                        },
//                        bringIntoViewRequester = bringIntoViewRequester,
//                        coroutineScope = coroutineScope,
                        error = amountError.value,
                        errorMessage = emptyErrorMsg,
                        floatsOnly = true
                    )
                }
            }


            Button(
                text = "Create Family",
                symbol = Icons.Default.Check,
                onClick = {
                    if (paymentData.familyName == ""){
                        nameError.value = true
                    } else {
                        nameError.value = false
                    }
                    if (paymentData.paymentId == ""){
                        idError.value = true
                    } else {
                        idError.value = false
                    }
                    if (paymentData.email == ""){
                        emailError.value = true
                    } else {
                        emailError.value = false
                    }
                    try{
                        LocalDate.parse(paymentData.paymentDate, LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() })
                        dateError.value = false
                    } catch (e: Exception) {
                        dateError.value = true
                    }
                    if (paymentData.amount == ""){
                        amountError.value = true
                    } else {
                        amountError.value = false
                    }
                    if(!nameError.value && !idError.value && !emailError.value && !dateError.value && !amountError.value) createFamily()
                },
                color = SuccessColor,
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}