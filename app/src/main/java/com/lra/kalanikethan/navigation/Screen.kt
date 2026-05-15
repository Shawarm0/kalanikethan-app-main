package com.lra.kalanikethan.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    object Dashboard : Screen("dashboard", Icons.Filled.Home, Icons.Outlined.Home)
    object SignIn : Screen("sign_in", Icons.Filled.PersonAdd, Icons.Outlined.PersonAdd)
    object Add : Screen("add", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline)
    object WhoseIn : Screen("who's_in", Icons.Filled.PersonSearch, Icons.Outlined.PersonSearch)
    object History : Screen("history", Icons.Filled.History, Icons.Outlined.HistoryToggleOff)
    object Payments : Screen("payments", Icons.Filled.Payment, Icons.Outlined.Payment)
    object Account : Screen("account", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
    object Class : Screen("class", Icons.Filled.Class, Icons.Outlined.Class)
    object EditClass : Screen("edit_class", Icons.Filled.EditNote, Icons.Outlined.EditNote)
    object EditFamily : Screen("edit_family/{familyId}", Icons.Filled.EditNote, Icons.Outlined.EditNote) {
        fun createRoute(familyId: String) = "edit_family/$familyId"
    }
    object PaymentHistory : Screen("payment_history", Icons.Filled.History, Icons.Outlined.History)
}
