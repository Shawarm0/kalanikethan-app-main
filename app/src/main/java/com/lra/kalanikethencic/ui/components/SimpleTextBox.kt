package com.lra.kalanikethencic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleDecoratedTextField(
    modifier: Modifier = Modifier.width(328.dp),
    text: String,
    placeholder: String = "Text",
    label: String? = null,
    onValueChange: (String) -> Unit = {},
    leadingIcon:  ImageVector? = null,
    trailingIcon: ImageVector? = null,
    clearbutton: Boolean = false,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
) {
    val text = remember { mutableStateOf(text) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val borderColor = if (isFocused) Color.Black.copy(alpha=0.7f) else Color(0xFFDCDEDD)
    val iconColor = if (isFocused) Color.Black.copy(alpha=0.7f) else Color.Gray



    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 0.dp)
            )
        }

        BasicTextField(
            value = text.value,
            modifier = modifier.wrapContentWidth().wrapContentHeight()
                .padding(0.dp)
                .clip(RoundedCornerShape(12.dp))
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            onValueChange = {
                            text.value = it
                            onValueChange(it)
                            },
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color.Gray,
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = "Leading Icon",
                            tint = iconColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Box {
                        if (text.value.isEmpty()) {

                            Text(
                                text = placeholder,
                                color = if (!isFocused) Color.Gray else Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (clearbutton && text.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                text.value = ""
                                onValueChange("")
                            },
                            modifier = Modifier.padding(0.dp)
                            .width(30.dp)
                            .height(20.dp),
                            interactionSource = remember { MutableInteractionSource() },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Text",
                                    tint = iconColor
                                )
                            }
                        )
                    }


                    if (trailingIcon != null) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Trailing Icon",
                            tint = iconColor
                        )
                    }
                }
            }
        )
    }
}

