package com.lra.kalanikethan.ui.components

import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.LightBoxBackground
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.util.convertLongToTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Instant


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
 * @param trailingIcon2 Optional trailing composable icon.
 * @param clearButton If true, shows a clear button when text is non-empty.
 * @param bringIntoViewRequester Used to scroll the field into view when focused.
 * @param coroutineScope The scope used to launch bring-into-view animations.
 */
fun SimpleDecoratedTextField2(
    modifier: Modifier = Modifier.width(328.dp),
    text: String,
    placeholder: String = "Text",
    label: String? = null,
    onValueChange: (String) -> Unit = {},
    leadingIcon: ImageVector? = null,
    isLeadingIconClickable: Boolean = false,
    trailingIcon: ImageVector? = null,
    trailingIcon2: @Composable (() -> Unit)? = null,
    clearButton: Boolean = false,
    passwordHidden: Boolean = false,
    error: Boolean = false,
    errorMessage: String? = null,
    floatsOnly: Boolean = false
) {


    // For the text boxes
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }


    // Maintain text state internally
    val text = remember { mutableStateOf(text) }

    // Used to observe interaction events such as focus
    val interactionSource = remember { MutableInteractionSource() }

    // Track whether the text field is currently focused
    val isFocused = interactionSource.collectIsFocusedAsState().value

    var showDatePicker by remember { mutableStateOf(false) }

    // Adjust border and icon color based on focus state
    val borderColor = if (isFocused && error) Color.Red else if (isFocused) Color.Black.copy(alpha = 0.7f) else if (error) Color(0xFFF19191) else Color(0xFFDCDEDD)
    val iconColor = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color.Gray

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Optional label displayed above the text field
        Row() {
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
            if (error) {
                if (errorMessage!=null){
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Red
                        )
                    )
                }
            }
        }

        // Core text input field with customization
        BasicTextField(
            value = text.value,
            keyboardOptions = if (floatsOnly) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = {
                if(floatsOnly){
                    if(it.matches(Regex("^\\d*\\.?\\d*$"))){
                        text.value = it
                        onValueChange(it)
                    }
                } else {
                    text.value = it
                    onValueChange(it) // Propagate the change upstream
                }
            },
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (isFocused) Color.Black.copy(alpha = 0.7f) else Color.Gray,
            ),
            modifier = Modifier
                .fillMaxWidth()  // Take all available width
                .heightIn(min = 48.dp)  // Or use fixed height if preferred
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
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
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
                            modifier = if (isLeadingIconClickable) Modifier.clickable(
                                onClick = {
                                    showDatePicker = true
                                }
                            ) else Modifier,
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
                    if (trailingIcon2 != null) {
                        trailingIcon2()
                    }
                }
            }
        )

                if (showDatePicker) {
                    val timePickerState = rememberTimePickerState(
                        is24Hour = false,
                    )

                    TimePickerDialog(
                        onDismiss = {
                            showDatePicker = false
                        },
                        onConfirm = {
                            text.value = "${timePickerState.hour}:${timePickerState.minute}"
                            showDatePicker = false
                            onValueChange(text.value)
                        },
                    ) {
                        Surface( // <- sets the background for the whole dialog, including buttons
                            color = Background
                        ) {
                            TimePicker(
                                modifier = Modifier.background(Background),
                                state = timePickerState,
                                colors = TimePickerColors(
                                    containerColor = Background,
                                    clockDialColor = Background,
                                    selectorColor = PrimaryLightColor,
                                    periodSelectorBorderColor = PrimaryLightColor,
                                    clockDialSelectedContentColor = Color.White,
                                    clockDialUnselectedContentColor = Color.Black,
                                    periodSelectorSelectedContainerColor = PrimaryLightColor,
                                    periodSelectorUnselectedContainerColor = Background,
                                    periodSelectorSelectedContentColor = Color.White,
                                    periodSelectorUnselectedContentColor = Color.Black,
                                    timeSelectorSelectedContainerColor = PrimaryLightColor,
                                    timeSelectorUnselectedContainerColor = Background,
                                    timeSelectorSelectedContentColor = Color.White,
                                    timeSelectorUnselectedContentColor = Color.Black
                                )
                            )
                        }

                    }
                }

    }
}







@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Background,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss", color = Color.Black)
            }
        },

        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK", color = Color.Black)
            }
        },
        text = { content() }
    )
}