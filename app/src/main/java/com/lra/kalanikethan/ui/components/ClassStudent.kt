package com.lra.kalanikethan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.theme.AccentColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText
import io.github.jan.supabase.realtime.Column

@Composable
fun ClassStudentComposable(
    modifier: Modifier = Modifier,
    name: String = "Student Name",
    isSelected: Boolean = false,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(color = if (isSelected) AccentColor else Color.White).clickable(
            onClick = {},
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                )
            )
        }
    }
}

@Composable
fun ClassStudentDisplay(
    modifier: Modifier = Modifier,
    name: String = "Student Name",
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp)).background(color = Color(0xFFE7EEF5)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = UnselectedButtonText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                )
            )
        }
    }
}




@Preview
@Composable
fun ClassStudentComposablePreview() {
    Column {
        ClassStudentComposable(
            modifier = Modifier.width(600.dp).height(60.dp),
            isSelected = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        ClassStudentComposable(
            modifier = Modifier.width(600.dp).height(60.dp),
            isSelected = true
        )


        Spacer(modifier = Modifier.height(10.dp))

        ClassStudentDisplay(
            modifier = Modifier.width(600.dp).height(60.dp),
            name = "Student Name"
        )
    }
}