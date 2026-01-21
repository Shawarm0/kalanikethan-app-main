package com.lra.kalanikethan.ui.screens.dashBoardViewModel

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.StudentsViewModel
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.ui.components.Button
import com.lra.kalanikethan.ui.components.ClassStudentComposable
import com.lra.kalanikethan.ui.components.ClassStudentDisplay
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField2
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import com.lra.kalanikethan.ui.theme.SuccessColor

/**
 * Composable function for editing class details and managing student assignments.
 *
 * This screen allows editing of class information (teacher name, type, start/end times)
 * and managing which students are assigned to the class. Features a split layout with
 * current class students on the left and a searchable list of all students on the right.
 *
 * @param viewModel The [DashBoardViewModel] that provides class data and manages student selections
 * @param signInViewModel The [SignInViewModel] that provides student data and search functionality
 * @param navController The [NavHostController] for navigation back to the dashboard
 */
@Composable
fun EditClass(
    viewModel: StudentsViewModel,
    navController: NavHostController
) {
    val thisClass = viewModel.thisClass.value ?: return

    // --- students state ---
    val studentsInClass =
        viewModel.selectedStudentsForActiveClass.collectAsState().value


    val allStudents = viewModel.displayedStudents.collectAsState(emptyList())
    val searchQuery = viewModel.searchQuery.value

    // --- load students for class ONCE ---
    LaunchedEffect(thisClass.classId) {
        viewModel.removeSearchQuery()
        viewModel.resetPendingUpdates()
        viewModel.loadStudentsForClass(thisClass.classId)
    }

    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        /* ───────────────────────── LEFT SECTION ───────────────────────── */

        Column {

            // Title
            Column(
                modifier = Modifier.padding(start = 53.dp, top = 14.dp)
            ) {
                Text(
                    "${thisClass.teacherName}'s Class",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .onGloballyPositioned {
                            textWidth = with(density) { it.size.width.toDp() }
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(Modifier.width(textWidth))
                Spacer(Modifier.height(14.dp))
            }

            // Class Details
            Column(modifier = Modifier.padding(top = 15.dp)) {

                Row(
                    modifier = Modifier.padding(start = 53.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimpleDecoratedTextField(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = thisClass.teacherName,
                        label = "Teacher Name",
                        placeholder = "Enter Teacher Name",
                        onValueChange = { thisClass.teacherName = it }
                    )

                    Spacer(Modifier.width(25.dp))

                    SimpleDecoratedTextField2(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = thisClass.startTime,
                        label = "Start Time",
                        placeholder = "Enter Start Time",
                        leadingIcon = Icons.Default.AccessTime,
                        onValueChange = { thisClass.startTime = it }
                    )
                }

                Spacer(Modifier.height(13.dp))

                Row(
                    modifier = Modifier.padding(start = 53.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimpleDecoratedTextField(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = thisClass.type,
                        label = "Type",
                        placeholder = "Enter Type",
                        onValueChange = { thisClass.type = it }
                    )

                    Spacer(Modifier.width(25.dp))

                    SimpleDecoratedTextField2(
                        modifier = Modifier.width(328.dp).height(60.dp),
                        text = thisClass.endTime,
                        label = "End Time",
                        placeholder = "Enter End Time",
                        leadingIcon = Icons.Default.AccessTime,
                        onValueChange = { thisClass.endTime = it }
                    )
                }
            }

            // Students in Class
            Column(
                modifier = Modifier.padding(start = 53.dp, top = 19.dp)
            ) {
                Text(
                    "Students",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(Modifier.width(textWidth))
                Spacer(Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(681.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(studentsInClass.sortedBy { it.firstName }) { student ->
                        ClassStudentDisplay(
                            modifier = Modifier.fillMaxWidth(),
                            name = "${student.firstName} ${student.lastName}"
                        )
                    }
                }
            }
        }

        VerticalDivider(modifier = Modifier.height(400.dp))

        /* ───────────────────────── RIGHT SECTION ───────────────────────── */

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 100.dp, end = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SimpleDecoratedTextField(
                text = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = "Search by firstname, ID or lastname...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.width(367.dp),
                clearButton = true
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allStudents.value) { student ->
                    val isSelected =
                        studentsInClass.any { it.studentId == student.studentId }

                    ClassStudentComposable(
                        modifier = Modifier.width(367.dp),
                        name = "${student.firstName} ${student.lastName}",
                        isSelected = isSelected,
                        onClickListener = {
                            viewModel.toggleStudentSelection(
                                thisClass.classId,
                                student.studentId ?: return@ClassStudentComposable,
                                isSelected = !isSelected
                            )
                        }
                    )
                }
            }

            Button(
                text = "Save",
                symbol = Icons.Default.Check,
                color = SuccessColor,
                modifier = Modifier.width(367.dp),
                onClick = {
                    navController.navigate(Screen.Dashboard.route)
                    viewModel.thisClass.value = thisClass
                    viewModel.updateClassState(thisClass.classId)
                }
            )
        }
    }
}
