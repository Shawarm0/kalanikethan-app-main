package com.lra.kalanikethan.ui.screens.dashBoardViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.StudentsViewModel
import com.lra.kalanikethan.ui.components.ClassBox
import com.lra.kalanikethan.util.isManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable function that displays a dashboard of classes for the current day.
 *
 * Shows a horizontal list of class boxes that can be clicked to navigate to class details
 * or edited (for managers). Features a dynamically sized title with matching divider.
 *
 * @param viewModel The [DashBoardViewModel] that provides class data
 * @param navController The [NavHostController] for navigation between screens
 * @param selectedIconChange Callback function to update the selected icon in the navigation
 */
@Composable
fun Dashboard(
    viewModel: StudentsViewModel,
    navController: NavHostController,
    selectedIconChange: (ImageVector) -> Unit = {}
) {
    val classes = viewModel.allClasses.collectAsState(emptyList())
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    Column( // This is the Title
        modifier = Modifier
            .wrapContentSize().padding(start = 53.dp, top = 14.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Capture the width of the Text
        Text(
            "Classes Today",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                // Convert width to dp
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        // HorizontalDivider with dynamic width equal to the Text width
        HorizontalDivider(modifier = Modifier.width(textWidth))
        Spacer(modifier = Modifier.height(33.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            items(classes.value) {thisClass ->
                ClassBox(
                    classData = thisClass,
                    isManager = isManager(),
                    onEditClick = { thisClass ->
                        viewModel.thisClass.value = thisClass
                        viewModel.resetPendingUpdates()
                        selectedIconChange(Screen.EditClass.filledIcon)
                        navController.navigate(Screen.EditClass.route)
                    },
                    onClassClick = { thisClass ->
                        viewModel.thisClass.value = thisClass
                        selectedIconChange(Screen.Class.filledIcon)
                        navController.navigate(Screen.Class.route)
                        scope.launch {
                            viewModel.isLoading.value = true
                            delay(500)
                            viewModel.isLoading.value = false
                        }
                    },
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

