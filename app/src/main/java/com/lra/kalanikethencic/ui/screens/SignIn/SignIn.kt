package com.lra.kalanikethencic.ui.screens.SignIn

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethencic.ui.components.StudentInfoCard
import kotlinx.coroutines.coroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignIn(viewModel: SignInViewModel = hiltViewModel()) {
    val students = viewModel.filteredStudents.collectAsState(emptyList())
    val searchQuery = viewModel.searchQuery.value
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SimpleDecoratedTextField(
            text = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = "Search by firstname, ID or lastname...",
            leadingIcon = Icons.Default.Search,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope,
            modifier = Modifier.width(1075.dp),
            clearbutton = true,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(students.value) { student ->
                StudentInfoCard(
                    studentData = student,
                    onSignInToggle = {
                        viewModel.updateStudent(student.copy(signedIn = !student.signedIn))
                    },
                    onAbsentClick = { },
                    onEditClick = { },
                )
            }
        }
    }
}
