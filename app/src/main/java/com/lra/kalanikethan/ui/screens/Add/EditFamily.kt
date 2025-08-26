package com.lra.kalanikethan.ui.screens.Add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.lra.kalanikethan.ui.components.Tab
import com.lra.kalanikethan.ui.components.TabBar
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

@Composable
fun EditFamily(addViewModel: AddViewModel, students: MutableList<Student>, parents:  MutableList<Parent>, paymentData: PaymentData ) {

    val students = students
    val parents = remember { mutableStateListOf<Parent>() }
    var tab by remember { mutableStateOf(Tab.Student) }

    // Single state object for payment data
    var paymentData by remember { mutableStateOf(PaymentData()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (tab) {
                Tab.Student -> StudentContent(
                    students = students,
                    onAddStudent = { students.add(it) }
                )
                Tab.Parents -> ParentContent(
                    parents = parents,
                    onAddParent = { parents.add(it) }
                )
                Tab.Payments -> PaymentContent(
                    paymentData = paymentData,
                    onPaymentDataChanged = { paymentData = it },
                    createFamily = {
                        addViewModel.createFamily(paymentData, parents, students)
                    }
                )
            }
        }
        TabBar(onTabSelected = { tab = it })
    }
}



@Composable
private fun StudentContent(
    students: MutableList<Student>,  // Changed to MutableList for adding items
    onAddStudent: (Student) -> Unit  // Callback for adding students
) {

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(students) { student ->
                StudentBox(
                    initialData = student,
                    onConfirm = { updatedStudent ->
                        val index = students.indexOf(student)
                        students[index] = updatedStudent
                    },
                    deleteStudent = {
                        students.remove(student)
                    },
                    editable = student.firstName == "" && student.lastName == "",
                    deleteIfCancelledOnFirstEdit = true
                )
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp).height(30.dp).wrapContentWidth().clip(RoundedCornerShape(15.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(
                    text = "Add Student",
                    symbol = Icons.Default.Add,
                    onClick = {
                        val newStudent = Student(
                            familyId = "",
                            firstName = "",
                            lastName = "",
                            birthdate = LocalDate(2000, 1, 1),
                            canWalkAlone = false,
                            dance = false,
                            singing = false,
                            music = false,
                            signedIn = false,
                        )
                        onAddStudent(newStudent)
                    }
                )
            }
        }
    }
}

@Composable
private fun ParentContent(
    parents: MutableList<Parent>,  // Changed to MutableList for adding items
    onAddParent: (Parent) -> Unit  // Callback for adding students
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(parents) { parent ->
                ParentBox(
                    initialData = parent,
                    onConfirm = { updatedParent ->
                        val index = parents.indexOf(parent)
                        parents[index] = updatedParent
                    },
                    deleteParent = {
                        parents.remove(parent)
                    },
                    editable = parent.firstName == "" && parent.lastName == "",
                )
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp).height(30.dp).wrapContentWidth().clip(RoundedCornerShape(15.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(
                    text = "Add Parent",
                    symbol = Icons.Default.Add,
                    onClick = {
                        val parent = Parent(
                            familyId = "",
                            firstName = "",
                            lastName = "",
                            phoneNumber = ""
                        )

                        onAddParent(parent)
                    }
                )
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