package com.lra.kalanikethencic.ui.screens

import android.icu.text.CaseMap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.lra.kalanikethencic.ui.components.Class

@Composable
fun Home() {
    var textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Column(
        modifier = Modifier.padding(start = 53.dp)
    ) {

        Column( // This is the Title
            modifier = Modifier
                .wrapContentSize().padding(top = 14.dp)
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





    }

}

