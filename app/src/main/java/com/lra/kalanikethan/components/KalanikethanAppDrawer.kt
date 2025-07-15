package com.lra.kalanikethan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.R
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.PrimaryLightColor
import com.lra.kalanikethan.ui.theme.UnselectedButtonText

/**
 * This is the custom app drawer that we have
 *
 * @param selectedScreen this is the current Screen that is selected
 * @param onScreenSelected this is a callback function that is called when a screen is selected, screen selection is handled in parent function
 *
 * @return The new Screen object that has been selected
 */
@Composable
fun KalanikethanAppDrawer(
    selectedScreen: String,
    onScreenSelected: (Screen) -> Unit
) {
    // This is the container that holds the whole app drawer together.
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(400.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {

        // This holds all the content inside the app
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // This is the title row at the top of the app drawer, it holds the Image and the Text at the top of the drawer
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // This is the logo in the top left
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(41.dp)
                        .clip(CircleShape)
                        .background(PrimaryLightColor)
                        .border(
                            border = BorderStroke(2.dp, PrimaryLightColor),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp)) // Gap between elements

                // This is the title text
                Column {
                    Text(text = "Kalanikethan App", style = MaterialTheme.typography.bodyLarge, fontSize = 16.sp, color = Color.Black) // overwrite default size
                    Text(text = "Welcome!", style = MaterialTheme.typography.bodyLarge, color = UnselectedButtonText)
                }
            }

            // Spacer between header and buttons
            Spacer(modifier = Modifier.height(16.dp))


            val drawerScreens = listOf(
                Screen.Dashboard,
                Screen.SignIn,
                Screen.Add,
                Screen.WhosIn,
                Screen.History,
                Screen.Payments,
                Screen.Account,
                Screen.Class,
            )

            Column {
                drawerScreens.forEach { screen ->
                    AppDrawerItem(
                        icon = if (selectedScreen == screen.route) screen.filledicon else screen.outlinedIcon,
                        text = screen.route.replaceFirstChar { it.uppercaseChar() }.replace("_", " "),
                        isSelected = selectedScreen == screen.route,
                        onClick = { onScreenSelected(screen) }
                    )
                }
            }

        }
    }
}


@Composable
fun AppDrawerItem(icon: ImageVector, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (isSelected) Background else Color.Transparent, shape = RoundedCornerShape(51.dp))
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
                tint = Color.Black,
                modifier = Modifier.padding(0.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
