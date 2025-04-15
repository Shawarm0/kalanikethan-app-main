package com.lra.kalanikethencic.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.ui.components.Class
import com.lra.kalanikethencic.ui.components.SimpleDecoratedTextField

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home() {
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val name = remember { mutableStateOf(String()) }
    val scrollState = rememberScrollState() // Remember the scroll state
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Column( // This is the Title
            modifier = Modifier
                .wrapContentSize().padding(start = 53.dp, end=53.dp, top = 14.dp)
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
        }
        Spacer(modifier = Modifier.height(23.dp))

        Class(
            teacher = "Dr Neelima",
            timeFrom = "15:30",
            timeTo = "16:40",
            className = "Dance",
        )


        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            leadingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            trailingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            leadingIcon = Icons.Default.Home,
            trailingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            label = "First Name",
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            label = "First Name",
            leadingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            label = "First Name",
            trailingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
        SimpleDecoratedTextField(
            text = name,
            placeholder = "e.g. John",
            label = "First Name",
            leadingIcon = Icons.Default.Home,
            trailingIcon = Icons.Default.Home,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )





    }
}
