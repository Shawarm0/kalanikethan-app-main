package com.lra.kalanikethan.ui.screens.DashBoardViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.ui.components.SimpleDecoratedTextField
import com.lra.kalanikethan.ui.screens.SignIn.SignInViewModel

@Composable
fun EditClass(
    viewModel: DashBoardViewModel,
    signInViewModel: SignInViewModel
)
{
    val thisClass = viewModel.thisClass.value
    val students = viewModel.getStudentsForClassFlow(thisClass.classId).collectAsState(emptyList())
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // Title Section with 113.dp padding from left
    Column(
        modifier = Modifier
            .padding(start = 53.dp, top = 14.dp)
            .fillMaxWidth()
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
        modifier = Modifier.wrapContentHeight().wrapContentWidth().padding(start = 53.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleDecoratedTextField(
                modifier = TODO(),
                text = thisClass.teacherName,
                placeholder = "Enter Teacher Name",
                label = "Teacher Name",
                onValueChange = {

                },

                passwordHidden = TODO(),
                error = TODO(),
                errorMessage = TODO(),
                floatsOnly = TODO()
            )

        }
    }
}