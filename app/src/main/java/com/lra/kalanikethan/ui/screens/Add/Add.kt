package com.lra.kalanikethan.ui.screens.Add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun Add(addViewModel: AddViewModel) {
    val students by addViewModel.students.collectAsState()
    val parents by addViewModel.parents.collectAsState()
    val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/'); year() }

    var paymentData by remember { mutableStateOf(PaymentData()) }
    val dateError = remember { mutableStateOf(false) }
    val invalid = remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(Tab.Student) }

    Scaffold(
        bottomBar = {
            TabBar(
                modifier = Modifier.fillMaxWidth(),
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 106.dp, vertical = 14.dp)
        ) {
            when (selectedTab) {
                Tab.Student -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Students", style = MaterialTheme.typography.displayLarge)
                            Button(
                                text = "Add student",
                                onClick = { addViewModel.createStudent() }
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            itemsIndexed(students, key = { _, s -> s.internalID!! }) { index, student ->
                                StudentBox(
                                    initialData = student,
                                    editable = true,
                                    deleteIfCancelledOnFirstEdit = true,
                                    title = "Student ${index + 1}",
                                    onConfirm = { confirmed ->
                                        val i = students.indexOf(student)
                                        addViewModel.updateStudent(i, confirmed.copy(internalID = student.internalID))
                                    },
                                    deleteStudent = { addViewModel.deleteStudent(student) }
                                )
                            }
                        }
                    }
                }

                Tab.Parents -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Parents", style = MaterialTheme.typography.displayLarge)
                            Button(
                                text = "Add parent",
                                onClick = { addViewModel.createParent() }
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            itemsIndexed(parents, key = { _, p -> p.internalID!! }) { index, parent ->
                                ParentBox(
                                    initialData = parent,
                                    editable = true,
                                    deleteIfCancelledOnFirstEdit = true,
                                    title = "Parent ${index + 1}",
                                    onConfirm = { confirmed ->
                                        val i = parents.indexOf(parent)
                                        addViewModel.updateParent(i, confirmed.copy(internalID = parent.internalID))
                                    },
                                    deleteParent = { addViewModel.deleteParent(parent) }
                                )
                            }
                        }
                    }
                }

                Tab.Payments -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Payment Details",
                                style = MaterialTheme.typography.displayLarge
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
                                        label = "Payment Date",
                                        isLeadingIconClickable = true,
                                        leadingIcon = Icons.Default.CalendarMonth,
                                        onValueChange = {
                                            if (it.isBlank()) {
                                                dateError.value = true
                                            }
                                            try {
                                                LocalDate.parse(it, formatter)
                                                paymentData = paymentData.copy(paymentDate = it)
                                                dateError.value = false
                                            } catch (_: Exception) {
                                                dateError.value = true
                                            }
                                        },
                                        error = dateError.value,
                                        errorMessage = "Invalid Date (DD/MM/YYYY)"
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
                                    if (checkError(students, parents, paymentData, dateError.value)) {
                                        addViewModel.createFamily(paymentData, parents, students)
                                    } else {
                                        invalid.value = true
                                    }
                                },
                                color = SuccessColor
                            )
                            if (invalid.value) {
                                Text(
                                    "Couldn't create family, some values are missing or invalid",
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun checkError(students: List<Student>, parents: List<Parent>, paymentData: PaymentData, dateError: Boolean): Boolean {
    if (dateError) return false
    for (student in students) {
        if (student.firstName.isBlank()) return false
        if (student.lastName.isBlank()) return false
    }
    for (parent in parents) {
        if (parent.firstName.isBlank()) return false
        if (parent.lastName.isBlank()) return false
    }
    if (paymentData.familyName.isBlank()) return false
    if (paymentData.paymentId.isBlank()) return false
    if (paymentData.amount.isBlank()) return false
    if (paymentData.email.isBlank()) return false
    return true
}
