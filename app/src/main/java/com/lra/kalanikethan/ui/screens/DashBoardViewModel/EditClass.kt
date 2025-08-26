package com.lra.kalanikethan.ui.screens.DashBoardViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.ClassStudentComposable
import com.lra.kalanikethan.ui.components.ClassStudentDisplay
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.screens.SignIn.SignInViewModel
import com.lra.kalanikethan.ui.theme.SuccessColor
import io.ktor.util.Hash.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlin.collections.mutableListOf
import kotlin.math.sign

@Composable
fun EditClass(
    viewModel: DashBoardViewModel,
    signInViewModel: SignInViewModel,
    navController: NavHostController
)
{
    val thisClass = viewModel.thisClass.value
    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    val allStudents = signInViewModel.displayedStudents.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        signInViewModel.removeSearchQuery()
        viewModel.getStudentsForClassFlow(thisClass.classId).collect {
            students = it
            for (student in it) {
                viewModel.toggleStudentSelection(thisClass.classId,student.studentId ?: return@collect, isSelected = true)
            }
        }
    }

    val searchQuery = signInViewModel.searchQuery.value
    val coroutineScope = rememberCoroutineScope()
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current




    // Title Section with 113.dp padding from left

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Column(
                modifier = Modifier
                    .padding(start = 53.dp, top = 14.dp)
                    .wrapContentWidth()
            ) {
                Text(
                    "${thisClass.teacherName}'s Class",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        textWidth = with(density) { coordinates.size.width.toDp() }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.width(textWidth))
                Spacer(modifier = Modifier.height(14.dp))
            }

            Column(
                modifier = Modifier
                    .wrapContentWidth().padding(top = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Column(
                    modifier = Modifier.wrapContentHeight().wrapContentWidth()
                        .padding(start = 53.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SimpleDecoratedTextField(
                            modifier = Modifier.width(328.dp).height(60.dp),
                            text = thisClass.teacherName,
                            placeholder = "Enter Teacher Name",
                            label = "Teacher Name",
                            onValueChange = {

                            },
                        )
                        Spacer(modifier = Modifier.width(25.dp))
                        SimpleDecoratedTextField(
                            modifier = Modifier.width(328.dp).height(60.dp),
                            text = thisClass.startTime.toString(),
                            placeholder = "Enter Start Time",
                            label = "Start Time",
                            onValueChange = {

                            },
                        )

                    }


                    Spacer(modifier = Modifier.height(13.dp))
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SimpleDecoratedTextField(
                            modifier = Modifier.width(328.dp).height(60.dp),
                            text = thisClass.type,
                            placeholder = "Enter Type",
                            label = "Type",
                            onValueChange = {

                            },
                        )
                        Spacer(modifier = Modifier.width(25.dp))
                        SimpleDecoratedTextField(
                            modifier = Modifier.width(328.dp).height(60.dp),
                            text = thisClass.endTime.toString(),
                            placeholder = "Enter End Time",
                            label = "End Time",
                            onValueChange = {

                            },
                        )

                    }
                }

                // Title Section with 113.dp padding from left
                Column(
                    modifier = Modifier
                        .padding(start = 53.dp, top = 19.dp)
                        .wrapContentSize()
                ) {
                    Column(
                        modifier = Modifier.wrapContentHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            "Students",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            ),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(modifier = Modifier.width(textWidth))
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight().wrapContentWidth()
                            .padding(top = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(students.sortedBy { it.firstName }) { student ->
                            ClassStudentDisplay(
                                modifier = Modifier.width(681.dp).wrapContentHeight(),
                                name = "${student.firstName} ${student.lastName}"
                            )

                        }

                    }
                }


            }
        }

        VerticalDivider(
            modifier = Modifier.height(400.dp)

        )


        Column(
            modifier = Modifier
                .fillMaxHeight().wrapContentWidth()
                .padding(top = 100.dp, end = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            SimpleDecoratedTextField(
                text = searchQuery,
                onValueChange = { signInViewModel.updateSearchQuery(it) },
                placeholder = "Search by firstname, ID or lastname...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.width(367.dp),
                clearButton = true,
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f).wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {

                items(allStudents.value) { student ->
                    ClassStudentComposable(
                        modifier = Modifier.width(367.dp).wrapContentHeight(),
                        name = "${student.firstName} ${student.lastName}",
                        isSelected = students.any { it.studentId == student.studentId },
                        onClickListener = {
                            if (students.any { it.studentId == student.studentId }) {

                                students = students.filter { it.studentId != student.studentId }

                                viewModel.toggleStudentSelection(
                                    thisClass.classId,
                                    student.studentId ?: return@ClassStudentComposable,
                                    isSelected = false
                                )

                            } else {
                                students += student
                                viewModel.toggleStudentSelection(
                                    thisClass.classId,
                                    student.studentId ?: return@ClassStudentComposable,
                                    isSelected = true
                                )
                            }
                        }
                    )
                }
            }

            Button(
                text = "Save",
                symbol = Icons.Default.Check,
                onClick = {
                    viewModel.updateClassState(thisClass.classId)
                    viewModel.thisClass.value = thisClass
                    navController.navigate(Screen.Dashboard.route)
                },
                color = SuccessColor,
                modifier = Modifier.width(367.dp)
            )
        }
    }
}