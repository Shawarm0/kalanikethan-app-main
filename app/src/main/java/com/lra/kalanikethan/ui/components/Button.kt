package com.lra.kalanikethan.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText


/**
 * This is our standard button composable.
 *
 *
 * @param text this is the text that is displayed in the button
 * @param symbol this is the icon that is displayed in the button
 * @param onClick this is a callback function that is called when the button is clicked
 * @param color this is the color of the button
 * @param modifier this is the modifier that is used to modify the button
 */
@Composable
fun Button(
    text: String,
    symbol: ImageVector? = null,
    onClick: () -> Unit = {},
    color: Color = ButtonColor,
    modifier: Modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
){
    // This is the background
    Surface(
        modifier = modifier
            .height(30.dp),
        onClick = onClick,
        tonalElevation = 4.dp,
        color = color,
        shape = RoundedCornerShape(15.dp),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        // This is the content of the button
        Row (
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if(symbol != null){
                Icon(symbol, contentDescription = "Symbol", modifier = Modifier.size(20.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFFFFFFF)
            )
        }
    }
}


/**
 * This is our selection button composable.
 *
 *
 * @param text this is the text that is displayed in the button
 * @param onClick this is a callback function that is called when the button is clicked
 * @param selected this is a boolean that is used to determine if the button is selected
 * @param width this is the default width that is used to affect the button
 */
@Composable
fun SelectionButton(
    text: String,
    onClick: (Boolean) -> Unit = {},
    selected: MutableState<Boolean> = mutableStateOf(false),
    width: Dp = 164.dp
) {
    var state by remember { selected }
    var onStateChange : (Boolean) -> Unit = {
        value -> state = value
    }
    // This is the background
    Box (
        modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(color = if(state) AccentColor else Color(0xFFE7EEF5))
        .height(36.dp)
        .width(width)
        .wrapContentSize(Alignment.Center)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                onClick(!state)
                onStateChange(!state)
            }
        )
        .padding(horizontal = 8.dp)
    ) {
        // This is the content
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = text, style = MaterialTheme.typography.titleSmall, color = if(state) Color(0xFFFFFFFF) else UnselectedButtonText)
        }
    }
}

@Preview
@Composable
fun ButtonInfo(){
    Column {
        Button("Hello", Icons.Default.Add)
        Spacer(modifier = Modifier.height(5.dp))
        SelectionButton("Hello")
    }
}



@Composable
fun SelectionButton2(
    text: String,
    onClick: () -> Unit = {},
    width: Dp = 164.dp,
    color: Color,
    textColor: Color
) {
    Box (
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = color)
            .height(36.dp)
            .width(width)
            .wrapContentSize(Alignment.Center)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick()
                }
            )
            .padding(horizontal = 8.dp)
    ) {
        // This is the content
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = text, style = MaterialTheme.typography.titleSmall, color = textColor)
        }
    }
}