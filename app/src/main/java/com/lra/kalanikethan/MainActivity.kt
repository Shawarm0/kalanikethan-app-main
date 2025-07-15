package com.lra.kalanikethan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lra.kalanikethan.components.KalanikethanAppDrawer
import com.lra.kalanikethan.screens.Home
import com.lra.kalanikethan.screens.SignIn
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.KalanikethanTheme
import kotlinx.coroutines.launch

/**
 * The main entry point of the app
 *
 * Sets up the navigation graph and handles initial app configurations
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     * This is where you should perform initial setup such as inflating layouts,
     * initializing components, and setting up Compose content or navigation.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the most recent state; otherwise, it is `null`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalanikethanTheme {
                KalanikethanApp()
            }
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     * This is followed by [onResume] if the activity comes to the foreground.
     */
    override fun onStart() {
        super.onStart()
        println("MainActivity: onStart() called")
    }

    /**
     * Called when the activity starts interacting with the user.
     * This is a good place to start animations or resume resources.
     */
    override fun onResume() {
        super.onResume()
        println("MainActivity: onResume() called")
    }

    /**
     * Called when the system is about to put the activity into the background.
     * Use this to pause animations or save UI-related data.
     */
    override fun onPause() {
        super.onPause()
        println("MainActivity: onPause() called")
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Stop any processes that shouldn't run in the background here.
     */
    override fun onStop() {
        super.onStop()
        println("MainActivity: onStop() called")
    }

    /**
     * Called before the activity is destroyed.
     * Use this for final cleanup like releasing resources.
     */
    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity: onDestroy() called")
    }

    /**
     * Called after the activity has been stopped and is restarting again.
     * This is followed by [onStart].
     */
    override fun onRestart() {
        super.onRestart()
        println("MainActivity: onRestart() called")
    }
}

/**
 * Represents all the screens/routes used in the application.
 *
 * Each screen is defined as a sealed object with,
 * - [route] a unique string,
 * - [filledicon] which is the icon when selected =
 * - [outlinedIcon] which is the icon when not selected.
 */
sealed class Screen(val route: String, val filledicon: ImageVector, val outlinedIcon: ImageVector) {
    object Dashboard : Screen("dashboard", Icons.Filled.Home, Icons.Outlined.Home)
    object SignIn : Screen("sign_in", Icons.Filled.PersonAdd, Icons.Outlined.PersonAdd)
    object Add : Screen("add", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline)
    object WhosIn : Screen("whos_in", Icons.Filled.PersonSearch, Icons.Outlined.PersonSearch)
    object History : Screen("history", Icons.Filled.History, Icons.Outlined.HistoryToggleOff)
    object Payments : Screen("payments", Icons.Filled.Payment, Icons.Outlined.Payment)
    object Account : Screen("account", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
    object Class : Screen("class", Icons.Filled.Class, Icons.Outlined.Class)
}


/**
 * The main composable function for the Kalanikethan App.
 *
 * This function sets up the navigation structure, including a [ModalNavigationDrawer]
 * and a [Scaffold] that contains the app content and navigation logic.
 *
 * It tracks the currently selected screen based on the navigation controller's backstack,
 * and allows for navigation between screens using a custom drawer component.
 *
 * Key elements:
 * - [ModalNavigationDrawer] for a side drawer layout.
 * - [androidx.navigation.NavController] and [NavHost] for screen routing.
 * - [Scaffold] to host the top-level layout structure of the app.
 *
 * The selected screen is derived from the current back stack, and updated when a drawer item is clicked.
 */
@Composable
fun KalanikethanApp() {
    // This stores the state of the navigation drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // This is required to handle the the navigation drawer not on the main thread
    val scope = rememberCoroutineScope()

    // This is to handle navigation between screens.
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedScreen = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    // This is to clear focus
    val focusManager = LocalFocusManager.current



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            KalanikethanAppDrawer(
                selectedScreen = selectedScreen,
                onScreenSelected = { newScreen ->
                    // This handles the closing animation after a new screen is selected
                    scope.launch {
                        @Suppress("DEPRECATION")
                        drawerState.animateTo(DrawerValue.Closed, tween(500))
                    }
                    selectedScreen = newScreen.route
                    // This handles navigation after a new screen is selected
                    navController.navigate(newScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    ) {
        // The Modal Navigation Drawer
        Scaffold(
            modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .clickable(
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        ) { innerpadding ->
            NavHost(navController,
                startDestination = selectedScreen,
                modifier = Modifier
                    .padding(innerpadding)
            ) {
                composable(route = Screen.Dashboard.route) { Home() }
                composable(route = Screen.SignIn.route) { SignIn() }
            }
        }
    }
}