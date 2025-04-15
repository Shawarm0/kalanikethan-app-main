package com.lra.kalanikethencic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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

@Composable
fun Class(teacher: String = "Teacher", timeFrom: String = "HH:mm", timeTo: String = "HH:mm", className: String = "Class") {
    val darkTheme = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp, // Figma blur roughly maps to elevation
                shape = RoundedCornerShape(0.dp), // or your desired shape
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp)).height(277.dp).width(404.dp)
            .background(color = if (darkTheme) Color.Black else Color.White),
    ) {
        Column( // At the top of the composable
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.wrapContentSize().padding(12.dp)
            ) {
                Text(text = teacher, color = if (darkTheme) Color.White else Color.Black, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "$timeFrom - $timeTo", color = if (darkTheme) Color.LightGray else Color(0xFF3D4D5C), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = className, color = if (darkTheme) Color.LightGray else Color(0xFF3D4D5C), style = MaterialTheme.typography.titleSmall )
            }
        }


        Button(
            text = "Go to Students",
            color = MaterialTheme.colorScheme.onPrimary,
            onClick = { /* Do something */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
}


