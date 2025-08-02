package com.lra.kalanikethan.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.CurrencyPound
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotApplyResult
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

@Composable
fun Add() {
    val students = remember { mutableStateListOf<Student>() }
    val parents = remember { mutableStateListOf<Parent>() }
    var tab by remember { mutableStateOf(Tab.Student) }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Content based on selected tab - now takes available space but leaves room for TabBar
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
                Tab.Payments -> PaymentContent()
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
                    editable = student.firstName == "" && student.lastName == ""
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
                            studentId = 1,
                            familyId = 1,
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
                            parentId = 1,
                            familyId = 1,
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

) {
    // For the text boxes
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

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
                        text = "",
                        placeholder = "Enter Payment ID",
                        label = "Payment ID",
                        onValueChange = {

                        },
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )

                    SimpleDecoratedTextField(
                        text = "",
                        placeholder = "Enter Email",
                        label = "Email",
                        onValueChange = {

                        },
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )


                }


                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {

                    SimpleDecoratedTextField(
                        text = "",
                        placeholder = "DD/MM/YYYY",
                        label = "Enter Payment Date",
                        onValueChange = {

                        },
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope,
                        trailingIcon = Icons.Default.CalendarMonth
                    )

                    SimpleDecoratedTextField(
                        text = "",
                        placeholder = "Enter Amount",
                        label = "Amount",
                        onValueChange = {

                        },
                        bringIntoViewRequester = bringIntoViewRequester,
                        coroutineScope = coroutineScope,
                    )


                }




            }

            Button(
                text = "Create Family",
                symbol = Icons.Default.Check,
                onClick = {

                },
                color = SuccessColor,
                modifier = Modifier.fillMaxWidth()
            )


        }
    }




}