package com.lra.kalanikethencic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lra.kalanikethencic.data.model.Class
import com.lra.kalanikethencic.util.convertLongToTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassBox(
    classData: Class,
    onClassClick: (Class) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()

    // Convert Long timestamps to formatted time strings
    val startTime = convertLongToTime(classData.startTime) // You'll need the extension function we discussed
    val endTime = convertLongToTime(classData.endTime)

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
                Text(
                    text = classData.teacherName,
                    color = if (darkTheme) Color.White else Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
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

        Button(
            text = "Go to Students",
            color = MaterialTheme.colorScheme.onPrimary,
            onClick = { onClassClick(classData) } ,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
}


