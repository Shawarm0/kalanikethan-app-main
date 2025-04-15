package com.lra.kalanikethencic.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.coroutines.launch

// Custom modifier I stole from https://stackoverflow.com/questions/68592618/how-to-add-border-on-bottom-only-in-jetpack-compose
fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
fun TopAppBar(
    icon: ImageVector,
    title: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onScreenSelected: (String) -> Unit,
    selectedScreen: String
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .bottomBorder(1.dp, Color(0xFFE5E8EB))
            .height(56.dp) // Optional: standard top bar height
    ) {
        // Left and right content using Row with space-between
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left - Drawer Menu Icon
            IconButton(onClick = {
                scope.launch {
                    drawerState.animateTo(
                        DrawerValue.Open,
                        tween(500)
                    )
                }
            }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }

            if (selectedScreen != "Account") {
                // Right - Account Info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onScreenSelected("Account") },
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                    }
                    if (!isPortrait) {
                        Text(
                            "FirstName",
                            style = MaterialTheme.typography.displaySmall,
                            fontSize = 13.sp
                        )
                    }
                }
            }

        }

        // Center - Title & Icon (centered absolutely)
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = "Title", modifier = Modifier.padding(end = 8.dp))
            Text(title, style = MaterialTheme.typography.displaySmall)
        }
    }
}
