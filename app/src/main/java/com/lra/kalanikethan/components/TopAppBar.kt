package com.lra.kalanikethan.components

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.util.bottomBorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * A custom top app bar composable that includes:
 * - A menu icon on the left to open a navigation drawer.
 * - A centered title with an optional icon.
 * - An account icon on the right (with optional name shown in landscape).
 *
 * This app bar adapts to screen orientation and screen insets, and supports drawer animation.
 *
 * @param icon The [ImageVector] displayed alongside the title in the center of the top bar.
 * @param title The string title to display, which is formatted (capitalized and underscores removed).
 * @param scope The [CoroutineScope] used to control the opening animation of the navigation drawer.
 * @param drawerState The [DrawerState] representing the current state of the drawer (open or closed).
 * @param onAccountSelected Callback invoked when the account icon is clicked.
 * @param selectedScreen The name of the currently selected screen; if it is `"account"`, the account icon is hidden.
 */
@Composable
fun TopAppBar(
    icon: ImageVector,
    title: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onAccountSelected: () -> Unit,
    selectedScreen: String
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val title = title.replaceFirstChar { it.uppercaseChar() }.replace("_", " ")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Background)
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

            if (selectedScreen != "account") {
                // Right - Account Info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onAccountSelected() },
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "account")
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
