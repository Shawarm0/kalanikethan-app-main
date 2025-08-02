package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
/**
 * A custom text field with decoration, label, placeholder, optional icons, and a clear button.
 *
 * This composable wraps a [BasicTextField] with stylized borders, focus-aware behavior, optional
 * leading/trailing icons, and a clear (X) button.
 *
 * @param modifier Modifier applied to the text field. Default width is 328.dp.
 * @param text Initial text value.
 * @param placeholder Placeholder text shown when input is empty.
 * @param label Optional label displayed above the text field.
 * @param onValueChange Lambda invoked when the text changes.
 * @param leadingIcon Optional leading [ImageVector] icon.
 * @param trailingIcon Optional trailing [ImageVector] icon.
 * @param clearButton If true, shows a clear button when text is non-empty.
 * @param bringIntoViewRequester Used to scroll the field into view when focused.
 * @param coroutineScope The scope used to launch bring-into-view animations.
 */
fun SimpleDecoratedTextField(
    modifier: Modifier = Modifier.width(328.dp),
    text: String,
    placeholder: String = "Text",
    label: String? = null,
    onValueChange: (String) -> Unit = {},
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    clearButton: Boolean = false,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
) {
    // Maintain text state internally
    val text = remember { mutableStateOf(text) }

    // Used to observe interaction events such as focus
    val interactionSource = remember { MutableInteractionSource() }

    // Track whether the text field is currently focused
    val isFocused = interactionSource.collectIsFocusedAsState().value

    // Adjust border and icon color based on focus state
    val borderColor = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color(0xFFDCDEDD)
    val iconColor = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color.Gray

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Optional label displayed above the text field
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

        // Core text input field with customization
        BasicTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                onValueChange(it) // Propagate the change upstream
            },
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color.Gray,
            ),
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(0.dp)
                .clip(RoundedCornerShape(12.dp))
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged { focusState ->
                    // Bring the field into view when focused
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = LightBoxBackground,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Optional leading icon
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = "Leading Icon",
                            tint = iconColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Input text field with placeholder fallback
                    Box {
                        if (text.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = if (!isFocused) Color.Gray else Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        innerTextField() // Draws the actual text field content
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Optional clear button (X) to reset text
                    if (clearButton && text.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                text.value = ""
                                onValueChange("")
                            },
                            modifier = Modifier
                                .padding(0.dp)
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

                    // Optional trailing icon
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


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun SimpleDecoratedTextFieldPreview() {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Label only
        SimpleDecoratedTextField(
            text = "",
            onValueChange = {},
            label = "Label Only",
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )

        // 2. Placeholder only, no label
        SimpleDecoratedTextField(
            text = "",
            onValueChange = {},
            placeholder = "Placeholder Only",
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )

        // 3. Label + trailing icon only
        SimpleDecoratedTextField(
            text = "",
            onValueChange = {},
            label = "Trailing Icon",
            trailingIcon = Icons.Default.Close,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )

        // 4. Label + leading icon only
        SimpleDecoratedTextField(
            text = "",
            onValueChange = {},
            label = "Leading Icon",
            leadingIcon = Icons.Default.Close,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )

        // 5. Label + clear button only
        val clearText1 = remember { mutableStateOf("Clearable") }
        SimpleDecoratedTextField(
            text = clearText1.value,
            onValueChange = { clearText1.value = it },
            label = "Clear Button",
            clearButton = true,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )

        // 6. All features: label + leading + trailing + clear button
        val clearText2 = remember { mutableStateOf("All Features") }
        SimpleDecoratedTextField(
            text = clearText2.value,
            onValueChange = { clearText2.value = it },
            label = "All Features",
            leadingIcon = Icons.Default.Close,
            trailingIcon = Icons.Default.Close,
            clearButton = true,
            bringIntoViewRequester = bringIntoViewRequester,
            coroutineScope = coroutineScope
        )
    }
}



