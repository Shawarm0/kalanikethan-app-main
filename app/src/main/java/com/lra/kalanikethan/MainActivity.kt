package com.lra.kalanikethan


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.ui.components.KalanikethanAppDrawer
import com.lra.kalanikethan.ui.components.TopAppBar
import com.lra.kalanikethan.ui.screens.Add.Add
import com.lra.kalanikethan.ui.screens.Add.AddViewModel
import com.lra.kalanikethan.ui.screens.Auth.AuthActivity
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.Classes
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.DashBoardViewModel
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.Dashboard
import com.lra.kalanikethan.ui.screens.dashBoardViewModel.EditClass
import com.lra.kalanikethan.ui.screens.History
import com.lra.kalanikethan.ui.screens.Payments
import com.lra.kalanikethan.ui.screens.signIn.SignIn
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import com.lra.kalanikethan.ui.screens.WhoseIn
import com.lra.kalanikethan.ui.theme.Background
import com.lra.kalanikethan.ui.theme.KalanikethanTheme
import com.lra.kalanikethan.util.imeBottomPaddingFraction
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

/**
 * The main entry point of the app
 *
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * Initialised repository and view models.
     * Checks for Authenticated user in the current session:
     *      - If not authenticated, redirects to  [Kalanikethan]
     *      - If authenticated, redirects to KalanikethanApp
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down, this contains the most recent state; otherwise, it is `null`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            val context = LocalContext.current
            val authState = client.auth.sessionStatus.collectAsState()
            Log.i("Auth-Main", "Auth state: $authState")

            val repository = remember { Repository() }
            val signInViewModel = remember { SignInViewModel(repository) }
            val addViewModel = remember  { AddViewModel(repository) }
            val dashboardViewModel = remember { DashBoardViewModel(repository, signInViewModel) }

            LaunchedEffect(Unit) {
                if (!SupabaseClientProvider.isUserStillValid()) {
                    client.auth.signOut(SignOutScope.GLOBAL)
                }
            }

            KalanikethanTheme {
                when (authState.value) {
                    is SessionStatus.Authenticated -> {
                        // User is authenticated
                        signInViewModel.initializeStudents()
                        dashboardViewModel.loadClasses()
                        KalanikethanApp(signInViewModel, addViewModel, dashboardViewModel)
                    }
                    SessionStatus.Initializing -> {
                        // Still loading, show loading UI
                        CircularProgressIndicator()
                    }
                    else -> {
                        // Not authenticated, redirect to AuthActivity
                        context.startActivity(
                            Intent(context, AuthActivity::class.java),
                            ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle()
                        )
                        finish()
                    }
                }
            }
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     * This is followed by [onResume] if the activity comes to the foreground.
     */
    override fun onStart() {
        super.onStart()
        enableEdgeToEdge()
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
 * - [filledIcon] which is the icon when selected =
 * - [outlinedIcon] which is the icon when not selected.
 */
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
    object EditFamily: Screen("edit_family", Icons.Filled.EditNote, Icons.Outlined.EditNote)
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
fun KalanikethanApp(
    signInViewModel: SignInViewModel,
    addViewModel: AddViewModel,
    dashboardViewModel: DashBoardViewModel
) {
    // This stores the state of the navigation drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // This is required to handle the the navigation drawer not on the main thread
    val scope = rememberCoroutineScope()

    // This is to handle navigation between screens.
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedScreen = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route
    var selectedIcon by remember { mutableStateOf(Screen.Dashboard.filledIcon) }
    // This is to clear focus
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            KalanikethanAppDrawer(
                selectedScreen = selectedScreen,
                onScreenSelected = { newScreen ->
                    // This handles the closing animation after a new screen is selected
                    scope.launch {
                        @Suppress("DEPRECATION") // This is to suppress the depreciation error.
                        drawerState.animateTo(DrawerValue.Closed, tween(500))
                    }
                    // This handles navigation after a new screen is selected
                    navController.navigate(newScreen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    selectedIcon = newScreen.filledIcon

                }
            )

        }
    ) {
        // The Modal Navigation Drawer
        Scaffold(
            topBar = {
                TopAppBar(
                    icon = selectedIcon,
                    selectedScreen = selectedScreen,
                    title = selectedScreen,
                    scope = scope,
                    drawerState = drawerState,
                    onAccountSelected = {
                        val intent = Intent(context, AuthActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            },
            modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .clickable(
                onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        ) { innerPadding ->
            NavHost(navController,
                startDestination = selectedScreen,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(innerPadding)
                    .imeBottomPaddingFraction(0.9f),

            ) {
                composable(route = Screen.Dashboard.route) { Dashboard(dashboardViewModel, navController, selectedIconChange = { icon ->
                    selectedIcon = icon
                }) }
                composable(route = Screen.SignIn.route) { SignIn(signInViewModel) }
                composable(route = Screen.Add.route) { Add(addViewModel) }
                composable(route = Screen.WhoseIn.route) { WhoseIn(signInViewModel) }
                composable(route = Screen.History.route) { History(signInViewModel) }
                composable(route = Screen.Payments.route) { Payments() }
                composable(route = Screen.Account.route) { }
                composable(route = Screen.Class.route) { Classes(dashboardViewModel, signInViewModel) }
                composable(route = Screen.EditClass.route) { EditClass(dashboardViewModel, signInViewModel, navController) }
                composable(route = Screen.EditFamily.route) {  }
            }
        }
    }
}