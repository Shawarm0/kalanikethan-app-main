package com.lra.kalanikethan.ui.screens.SignIn

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.components.StudentInfoCard
import kotlinx.coroutines.launch


/**
 * The sign in screen for the app.
 *
 * This screen displays a list of students and allows users to sign in or sign out.
 * It uses a [LazyColumn] to display the list of students and a [SimpleDecoratedTextField]
 * It uses [LaunchedEffect] to initialise the students realTime channel in the [SignInViewModel]
 *
 * @param viewModel The view model for the sign in screen.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignIn(viewModel: SignInViewModel) {
    val students = viewModel.displayedStudents.collectAsState(emptyList())
    val searchQuery = viewModel.searchQuery.value
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()



    // Use DisposableEffect to handle channel lifecycle
    LaunchedEffect(Unit) {
        // Create the channel when the composable enters composition
        viewModel.initialiseStudentsChannel()
        viewModel.removeSearchQuery()
    }

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
            clearButton = true,
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
                        viewModel.signIn(it)
                    },
                    onAbsentClick = { },
                    onEditClick = {
                        coroutineScope.launch {
                            ChannelManager.unsubscribeFromAllChannels()
                        }
                    },
                )
            }
        }
    }
}