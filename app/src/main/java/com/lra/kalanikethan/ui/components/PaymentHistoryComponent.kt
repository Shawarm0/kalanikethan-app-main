package com.lra.kalanikethan.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.ui.theme.ErrorColor
import com.lra.kalanikethan.ui.theme.SuccessColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText

@Composable
fun PaymentHistoryComponent(
    modifier: Modifier = Modifier
        .width(1075.dp)
        .wrapContentHeight()
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .clip(RoundedCornerShape(12.dp))
        .background(Color.White),
    month: String,
    data: List<PaymentHistory>,
) {
    var isExpanded by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .clickable(
                    onClick = { },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = month,
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 24.sp
                    )
                )

            }

            // Expandable Events
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    for (event in data) {
                        PaymentEventComponent(
                            data = event
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun PaymentEventComponent(
    data: PaymentHistory,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Due Date Column - Fixed width
        Column(
            modifier = Modifier.width(200.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Due Date:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "${data.due_date.day}/${data.due_date.monthNumber}/${data.due_date.year}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Amount - Fixed width
        Column(
            modifier = Modifier.width(150.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Amount:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "£${data.amount}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        // Paid Colunm - Fixed width
        Column(
            modifier = Modifier.width(150.dp), // Fixed width
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paid:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = UnselectedButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = if (data.paid) "YES" else "NO",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (data.paid) SuccessColor else ErrorColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}