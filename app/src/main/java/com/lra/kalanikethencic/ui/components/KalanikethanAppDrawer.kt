package com.lra.kalanikethencic.ui.components

import android.view.Window
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethencic.R

@Composable
fun KalanikethanAppDrawer(selectedScreen: String, onScreenSelected: (String) -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    Column( // This is purely for the background
        modifier = Modifier
            .fillMaxHeight().width(400.dp)
            .background(if (darkTheme) Color(0xFF2A2630) else Color.White, shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column( // This is the content Frame
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row( // This is the Title Row
                modifier = Modifier.fillMaxWidth()
            ) {
                // This is the logo in the top left
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(41.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .border(
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp)) // Gap between elements

                Column { // This is the title text
                    Text(text = "Kalanikethan App", style = MaterialTheme.typography.bodyLarge, fontSize = 16.sp, color = if (darkTheme) Color.White else Color.Black) // overwrite default size
                    Text(text = "Welcome!", style = MaterialTheme.typography.bodyLarge, color = if (darkTheme) Color.LightGray else Color(0xFF3D4D5C))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppDrawerItem(
                icon = if (selectedScreen == "Dashboard") Icons.Filled.Home else Icons.Outlined.Home,
                text = "Dashboard",
                isSelected = if (selectedScreen == "Dashboard") true else false,
                onClick = { onScreenSelected("Dashboard") }
            )
            AppDrawerItem(
                icon = if (selectedScreen == "Sign In") Icons.Filled.PersonAdd else Icons.Outlined.PersonAdd,
                text = "Sign In",
                isSelected = if (selectedScreen == "Sign In") true else false,
                onClick = { onScreenSelected("Sign In") }
            )
            AppDrawerItem(
                icon = if (selectedScreen == "Add") Icons.Filled.AddCircle else Icons.Outlined.AddCircleOutline,
                text = "Add",
                isSelected = if (selectedScreen == "Add") true else false,
                onClick = { onScreenSelected("Add") }
            )
            AppDrawerItem(
                icon = if (selectedScreen == "Who's In") Icons.Filled.PersonSearch else Icons.Outlined.PersonSearch,
                text = "Who's In",
                isSelected = if (selectedScreen == "Who's In") true else false,
                onClick = { onScreenSelected("Who's In") }
            )
            AppDrawerItem(
                icon = if (selectedScreen == "History") Icons.Filled.History else Icons.Outlined.HistoryToggleOff,
                text = "History",
                isSelected = if (selectedScreen == "History") true else false,
                onClick = { onScreenSelected("History") }
            )
            AppDrawerItem(
                icon = if (selectedScreen == "Payments") Icons.Filled.Payment else Icons.Outlined.Payment,
                text = "Payments",
                isSelected = if (selectedScreen == "Payments") true else false,
                onClick = { onScreenSelected("Payments") }
            )

        }
    }
}


@Composable
fun AppDrawerItem(icon: ImageVector, text: String, isSelected: Boolean, onClick: () -> Unit) {
    val background = if (isSelected && !isSystemInDarkTheme()) MaterialTheme.colorScheme.background else if (isSelected && isSystemInDarkTheme()) Color(0xFF3E345C) else Color.Transparent
    val darkTheme = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = background, shape = RoundedCornerShape(51.dp))
            .clip(RoundedCornerShape(51.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, // Track the interaction
                indication = null, // Removes the ripple effect
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (darkTheme) Color.White else Color.Black,
                modifier = Modifier.padding(0.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (darkTheme) Color.White else Color.Black
            )
        }
    }
}
