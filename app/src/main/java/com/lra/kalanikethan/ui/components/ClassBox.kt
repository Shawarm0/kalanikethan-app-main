package com.lra.kalanikethan.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.ui.theme.ButtonColor
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.util.bottomBorder
import com.lra.kalanikethan.util.convertLongToTime
import com.lra.kalanikethan.util.isCurrentTimeWithinRange

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassBox(
    modifier: Modifier = Modifier,
    isManager: Boolean = false,
    classData: Class,
    onClassClick: (Class) -> Unit = {},
    onEditClick: (Class) -> Unit = {},
) {
    val darkTheme = false

    // Convert Long timestamps to formatted time strings
    val startTime = convertLongToTime(classData.startTime) // You'll need the extension function we discussed
    val endTime = convertLongToTime(classData.endTime)
    val istime = isCurrentTimeWithinRange(classData.startTime, classData.endTime)

    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp))
            .height(277.dp)
            .width(404.dp)
            .background(color = if (darkTheme) Color.Black else Color.White)
            .then(
                if (istime) Modifier.bottomBorder(3.dp, PrimaryLightColor)
                else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classData.teacherName,
                        color = if (darkTheme) Color.White else Color.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (isManager) {
                        Text(
                            modifier = Modifier.clickable(
                                onClick = {
                                    onEditClick(classData)
                                },
                                interactionSource = remember { MutableInteractionSource() }, // Track the interaction
                                indication = null, // Removes the ripple effect
                            ),
                            text = "Edit Class",
                            color = PrimaryLightColor,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 18.sp,
                            ),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$startTime - $endTime",
                    color = if (darkTheme) Color.LightGray else Color(0xFF3D4D5C),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = classData.type,
                    color = if (darkTheme) Color.LightGray else Color(0xFF3D4D5C),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        if (istime) {
            Text(
                text = "ONGOING",
                color = Color(0xFF3D4D5C),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomStart)
            )
        }

        Button(
            text = "Go to Students",
            color = ButtonColor,
            onClick = { onClassClick(classData) } ,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
}


