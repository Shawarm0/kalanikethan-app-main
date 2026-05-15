package com.lra.kalanikethan.ui.screens.editfamily

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.ParentBox
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentBox
import com.lra.kalanikethan.ui.components.Tab
import com.lra.kalanikethan.ui.components.TabBar
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText

@Composable
fun EditFamily(
    editFamilyViewModel: EditFamilyViewModel,
    familyId: String,
    onNavigateBack: () -> Unit = {}
) {
    val students by editFamilyViewModel.students.collectAsState()
    val parents by editFamilyViewModel.parents.collectAsState()
    val family by editFamilyViewModel.family.collectAsState()
    val isLoading by editFamilyViewModel.isLoading.collectAsState()
    var selectedTab by remember { mutableStateOf(Tab.Student) }

    LaunchedEffect(familyId) {
        editFamilyViewModel.loadFamily(familyId)
    }

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
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
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
                                    onClick = { editFamilyViewModel.addStudentLocally() }
                                )
                            }
                            Spacer(Modifier.height(14.dp))
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                itemsIndexed(
                                    students,
                                    key = { _, student -> student.studentId ?: 0 }
                                ) { index, student ->
                                    val isNew = student.studentId != null && student.studentId < 0
                                    StudentBox(
                                        initialData = student,
                                        onConfirm = { editFamilyViewModel.confirmStudent(it) },
                                        deleteStudent = {
                                            editFamilyViewModel.deleteStudent(student)
                                        },
                                        editable = isNew,
                                        deleteIfCancelledOnFirstEdit = isNew,
                                        title = "Student ${index + 1}"
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
                                    onClick = { editFamilyViewModel.addParentLocally() }
                                )
                            }
                            Spacer(Modifier.height(14.dp))
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                itemsIndexed(
                                    parents,
                                    key = { _, parent -> parent.parentId ?: 0 }
                                ) { index, parent ->
                                    val isNew = parent.parentId != null && parent.parentId < 0
                                    ParentBox(
                                        initialData = parent,
                                        onConfirm = { editFamilyViewModel.confirmParent(it) },
                                        deleteParent = {
                                            editFamilyViewModel.deleteParent(parent)
                                        },
                                        editable = isNew,
                                        deleteIfCancelledOnFirstEdit = isNew,
                                        title = "Parent ${index + 1}"
                                    )
                                }
                            }
                        }
                    }

                    Tab.Payments -> {
                        var familyName by remember(family) {
                            mutableStateOf(family?.familyName ?: "")
                        }
                        var email by remember(family) {
                            mutableStateOf(family?.email ?: "")
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
                                Text(
                                    text = "Family Details",
                                    style = MaterialTheme.typography.displayLarge
                                )
                                Text(
                                    text = "Edit the family information below.",
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
                                            text = familyName,
                                            placeholder = "Enter Family Name",
                                            label = "Family Name",
                                            onValueChange = { familyName = it },
                                            error = familyName.isBlank(),
                                            errorMessage = "Cannot be empty"
                                        )
                                        SimpleDecoratedTextField(
                                            text = email,
                                            placeholder = "Enter Email",
                                            label = "Email",
                                            onValueChange = { email = it },
                                            error = email.isBlank(),
                                            errorMessage = "Cannot be empty"
                                        )
                                    }
                                }

                                Button(
                                    text = "Save Changes",
                                    symbol = Icons.Default.Check,
                                    onClick = {
                                        if (familyName.isNotBlank() && email.isNotBlank()) {
                                            editFamilyViewModel.saveAll(familyName, email) {
                                                onNavigateBack()
                                            }
                                        }
                                    },
                                    color = SuccessColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
