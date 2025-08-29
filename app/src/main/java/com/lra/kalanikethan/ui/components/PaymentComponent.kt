package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.screens.Add.PaymentData
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText

@Composable
fun PaymentComponent(
    modifier: Modifier = Modifier.width(1075.dp)
        .height(108.dp)
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        ).clip(RoundedCornerShape(12.dp)).background(Color.White),
    data: PaymentData,
    onConfirmClick: () -> Unit = {},
    onSendReminderClick: () -> Unit = {},
    onIncorrectClick: () -> Unit = {},
    onViewHistoryClick: () -> Unit = {},
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = "Hello",
                        tint = Color.Unspecified,
                        modifier = Modifier.width(38.dp).height(38.dp)
                    )

                    Column(
                        modifier = Modifier.height(38.dp).wrapContentWidth(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = data.familyName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )

                            Text(
                                text = "View History",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = PrimaryLightColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        onViewHistoryClick()
                                    },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                modifier = Modifier.padding(end = 15.dp),
                                text = "ID: ${data.paymentId}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = UnselectedButtonText,
                                    fontWeight = FontWeight.Light,
                                )
                            )

                            VerticalDivider()

                            Text(
                                modifier = Modifier.padding(start = 15.dp),
                                text = "£${data.amount}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = UnselectedButtonText,
                                    fontWeight = FontWeight.Light,
                                )
                            )
                        }
                    }
                }

                InfoBox(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight().clip(RoundedCornerShape(16.dp)),
                    text = "DD/MM/YYYY",
                    fontSize = 14.sp,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    text = "Confirm Payment",
                    onClick = {
                        onConfirmClick()
                    },
                    color = SuccessColor,
                )

                Button(
                    text = "Send Reminder",
                    onClick = {
                        onSendReminderClick()
                    },
                    color = AccentColor,
                )

                Button(
                    text = "Incorrect Amount Paid",
                    onClick = {
                        onIncorrectClick()
                    },
                    color = ErrorColor,
                )
            }


        }
    }
}



@Preview
@Composable
fun PaymentComponentPreview(

) {
    PaymentComponent(
        data = PaymentData(
            familyName = "Family Name",
            paymentId = "NV14LLG",
            amount = "12.00"
        ),
    )
}